package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.data.mapper.ThumbnailFileNameMapper
import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.io.File
import java.time.LocalDate

class VideosDao(
    private val context: Context,
    private val settingsDao: SettingsDao,
    private val videoFileNameMapper: VideoFileNameMapper,
    private val thumbnailFileNameMapper: ThumbnailFileNameMapper,
    private val videoThumbnailExtractor: VideoThumbnailExtractor,
) {
    private var videosDir = File(context.filesDir, "videos").apply {
        mkdirs()
    }

    private val thumbnailsDir = File(context.filesDir, "thumbnails").apply {
        mkdirs()
    }

    private val allVideosMutable = MutableStateFlow(loadAllVideos())
    private val removableStorageAvailableMutable = MutableStateFlow(isRemovableStorageAvailable())

    /** A Flow of all video files in the diary, unordered */
    val allVideos = allVideosMutable.asStateFlow()
    val removableStorageAvailable = removableStorageAvailableMutable.asStateFlow()

    suspend fun initialiseStorageLocation() {
        val location = settingsDao.getVideoStorageLocationFlow().first()
        videosDir = getVideosDir(location) ?: getVideosDir(VideoStorageLocation.Internal)!!
        videosDir.mkdirs()
        refreshVideosState()
    }

    fun isRemovableStorageAvailable(): Boolean = getVideosDir(VideoStorageLocation.RemovableStorage) != null

    /** Moves all diary videos before changing the active storage location. */
    suspend fun moveVideosTo(location: VideoStorageLocation): Result<Unit> = runCatching {
        val destinationDir = getVideosDir(location)
            ?: error("Removable storage is not available")
        if (videosDir.canonicalPath == destinationDir.canonicalPath) return Result.success(Unit)

        destinationDir.mkdirs()
        check(destinationDir.isDirectory) { "Could not create destination directory" }
        val sourceFiles = videosDir.listFiles()?.toList().orEmpty()
        sourceFiles.forEach { source ->
            source.copyTo(File(destinationDir, source.name), overwrite = true)
        }
        sourceFiles.forEach { source ->
            check(source.delete()) { "Could not remove ${source.name} from the old location" }
        }
        videosDir = destinationDir
        removableStorageAvailableMutable.value = isRemovableStorageAvailable()
        refreshVideosState()
    }

    fun deleteVideo(date: LocalDate) {
        val file = getVideoFile(date)
        file.delete()
        getThumbnailFile(date).delete()
        refreshVideosState()
    }

    /**
     * Deletes a freshly recorded video from MediaStore (e.g. one the user chose not to keep,
     * after comparing it against an existing video for that day).
     */
    fun discardMediaStoreVideo(videoContentUri: Uri) {
        try {
            context.contentResolver.delete(videoContentUri, null, null)
        } catch (e: Exception) {
            Logger.error("Error discarding video", e)
        }
    }

    fun persistVideo(videoContentUri: Uri, date: LocalDate) {
        try {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            val cursor =
                context.contentResolver.query(videoContentUri, projection, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val sourcePath = cursor.getString(columnIndex)
                val sourceFile = File(sourcePath)

                val destinationFile = getVideoFile(date)
                destinationFile.parentFile?.mkdirs()
                if (destinationFile.exists()) {
                    destinationFile.delete()
                }

                sourceFile.copyTo(destinationFile)
                videoThumbnailExtractor.extractFirstFrame(destinationFile, getThumbnailFile(date))
                refreshVideosState()
            } else {
                Logger.error("Failed to get video path from content URI")
            }

            cursor?.close()
        } catch (e: Exception) {
            Logger.error("Error copying video", e)
        }
    }

    /**
     * Deletes all existing videos and creates placeholder video files for [dates], copied from
     * the `sample_video` raw resource. Used to fill the diary with mock data for debugging.
     */
    fun fillWithMockVideos(dates: List<LocalDate>) {
        videosDir.listFiles()?.forEach { it.delete() }
        thumbnailsDir.listFiles()?.forEach { it.delete() }
        dates.forEach { date ->
            val videoFile = getVideoFile(date)
            context.resources.openRawResource(R.raw.sample_video).use { input ->
                videoFile.outputStream().use { output -> input.copyTo(output) }
            }
            videoThumbnailExtractor.extractFirstFrame(videoFile, getThumbnailFile(date))
        }
        refreshVideosState()
    }

    fun getVideoFile(date: LocalDate) = getVideoFile(getVideoFileName(date))

    fun getVideoFileIfExists(date: LocalDate): File? {
        val file = getVideoFile(getVideoFileName(date))
        return if (file.exists()) file else null
    }

    fun getThumbnailFileIfExists(date: LocalDate): File? {
        val file = getThumbnailFile(date)
        return if (file.exists()) file else null
    }

    /**
     * Generates thumbnails for any persisted videos that don't yet have one.
     * Intended to be run once on app startup, to backfill videos that were
     * saved before thumbnail generation was introduced.
     */
    fun generateMissingThumbnails() {
        generateThumbnails(onlyMissing = true)
    }

    /**
     * Deletes all existing thumbnails, then regenerates a thumbnail for every persisted video.
     */
    fun resyncAllThumbnails() {
        thumbnailsDir.listFiles()?.forEach { it.delete() }
        generateThumbnails(onlyMissing = false)
    }

    private fun generateThumbnails(onlyMissing: Boolean) {
        val videoFiles = videosDir.listFiles() ?: return
        var generatedAny = false
        videoFiles.forEach { videoFile ->
            val date = try {
                videoFileNameMapper.nameToDate(videoFile.name)
            } catch (e: Exception) {
                Logger.warning("Skipping video with unrecognised file name: ${videoFile.name}", e)
                return@forEach
            }

            val thumbnailFile = getThumbnailFile(date)
            if (!onlyMissing || !thumbnailFile.exists()) {
                videoThumbnailExtractor.extractFirstFrame(videoFile, thumbnailFile)
                generatedAny = true
            }
        }

        if (generatedAny) {
            refreshVideosState()
        }
    }

    private fun getVideoFile(name: String) = File(videosDir, name)

    private fun getVideosDir(location: VideoStorageLocation): File? = when (location) {
        VideoStorageLocation.Internal -> File(context.filesDir, "videos")
        VideoStorageLocation.RemovableStorage -> context.getExternalFilesDirs(null)
            .firstOrNull { directory ->
                directory != null &&
                    Environment.isExternalStorageRemovable(directory) &&
                    Environment.getExternalStorageState(directory) == Environment.MEDIA_MOUNTED
            }
            ?.let { File(it, "videos") }
        is VideoStorageLocation.Custom -> {
            val uri = Uri.parse(location.uriString)
            getFileFromUri(context, uri)
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        if (uri.scheme == "file") {
            return uri.path?.let { File(it) }
        }
        if (uri.scheme == "content") {
            val docId = try {
                android.provider.DocumentsContract.getTreeDocumentId(uri)
            } catch (e: Exception) {
                try {
                    android.provider.DocumentsContract.getDocumentId(uri)
                } catch (e: Exception) {
                    null
                }
            }
            if (docId != null) {
                val split = docId.split(":")
                val type = split[0]
                val relativePath = if (split.size > 1) split[1] else ""

                if ("primary".equals(type, ignoreCase = true)) {
                    return File(Environment.getExternalStorageDirectory(), relativePath)
                } else {
                    val externalDirs = context.getExternalFilesDirs(null)
                    for (dir in externalDirs) {
                        if (dir != null) {
                            val path = dir.absolutePath
                            val volumePath = path.substringBefore("/Android/data/")
                            if (volumePath.contains(type, ignoreCase = true)) {
                                return File(volumePath, relativePath)
                            }
                        }
                    }
                    return File("/storage/$type/$relativePath")
                }
            }
        }
        return null
    }

    private fun getVideoFileName(date: LocalDate) = videoFileNameMapper.dateToName(date)

    private fun getThumbnailFile(date: LocalDate) =
        File(thumbnailsDir, thumbnailFileNameMapper.dateToName(date))

    /** Should be invoked whenever the persisted video files change in any way */
    private fun refreshVideosState() {
        allVideosMutable.value = loadAllVideos()
    }

    private fun loadAllVideos(): List<File> {
        val files = videosDir.listFiles() ?: return emptyList()
        return files.toList()
    }
}
