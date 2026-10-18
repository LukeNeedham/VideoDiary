package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.data.mapper.ThumbnailFileNameMapper
import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.common.logger.Logger
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate

class VideosDao(
    private val context: Context,
    private val settingsDao: SettingsDao,
    private val videoFileNameMapper: VideoFileNameMapper,
    private val thumbnailFileNameMapper: ThumbnailFileNameMapper,
    private val videoThumbnailExtractor: VideoThumbnailExtractor,
) {
    private val internalVideosDir = File(context.filesDir, "videos").apply {
        mkdirs()
    }

    /** The folder videos are currently read from and written to */
    @Volatile
    private var videosDir = internalVideosDir

    /** Held briefly by anything that changes the video files, and while a move is committed */
    private val filesLock = Any()

    private val thumbnailsDir = File(context.filesDir, "thumbnails").apply {
        mkdirs()
    }

    private val allVideosMutable = MutableStateFlow(loadAllVideos())
    private val removableStorageAvailableMutable = MutableStateFlow(isRemovableStorageAvailable())
    private val usingFallbackStorageMutable = MutableStateFlow(false)

    /** A Flow of all video files in the diary, unordered */
    val allVideos = allVideosMutable.asStateFlow()

    val removableStorageAvailable = removableStorageAvailableMutable.asStateFlow()

    /**
     * True when the chosen location is the SD card but it is not available right now,
     * so internal storage is being used until it comes back.
     */
    val usingFallbackStorage = usingFallbackStorageMutable.asStateFlow()

    /**
     * Points the diary at the saved storage location. Also re-checks whether the SD card is
     * available, so call it again when the SD card may have been inserted or removed.
     *
     * If the saved location is the SD card but it is missing, internal storage is used for now.
     * Once the card is back, any videos recorded in the meantime are merged onto it.
     */
    suspend fun initialiseStorageLocation() {
        val location = settingsDao.getVideoStorageLocationFlow().first()
        removableStorageAvailableMutable.value = isRemovableStorageAvailable()

        val wantedDir = getVideosDir(location)
        if (wantedDir == null) {
            usingFallbackStorageMutable.value = true
            switchVideosDir(internalVideosDir)
            return
        }
        usingFallbackStorageMutable.value = false

        // Videos may be in the other folder after an interrupted move, or after a fallback
        val otherDir = if (wantedDir == internalVideosDir) {
            getVideosDir(VideoStorageLocation.RemovableStorage)
        } else {
            internalVideosDir
        }
        if (otherDir != null && otherDir != wantedDir) {
            try {
                mergeInto(wantedDir, from = otherDir)
            } catch (e: Exception) {
                Logger.w("Could not merge videos into $wantedDir", e)
            }
        }
        switchVideosDir(wantedDir)
    }

    fun isRemovableStorageAvailable(): Boolean =
        getVideosDir(VideoStorageLocation.RemovableStorage) != null

    /**
     * Moves all diary videos to [location] and saves it as the new storage location.
     * On failure nothing changes: the videos stay where they were.
     *
     * This keeps going if the caller is cancelled, so a move is never left half-finished.
     */
    suspend fun moveVideosTo(location: VideoStorageLocation): Result<Unit> =
        withContext(NonCancellable) {
            runCatching {
                val destinationDir = getVideosDir(location)
                    ?: error("Removable storage is not available")
                val previousLocation = settingsDao.getVideoStorageLocationFlow().first()
                val sourceDir = videosDir
                if (sourceDir.canonicalPath == destinationDir.canonicalPath) {
                    settingsDao.setVideoStorageLocation(location)
                    usingFallbackStorageMutable.value = false
                    return@runCatching
                }

                val transfer = VideoFolderTransfer.copy(
                    from = sourceDir,
                    to = destinationDir,
                    failOnConflict = true,
                )
                try {
                    settingsDao.setVideoStorageLocation(location)
                    try {
                        synchronized(filesLock) {
                            // A video was recorded or deleted during the copy
                            check(transfer.isSourceUnchanged()) {
                                "The diary changed while videos were being moved"
                            }
                            videosDir = destinationDir
                        }
                    } catch (e: Throwable) {
                        settingsDao.setVideoStorageLocation(previousLocation)
                        throw e
                    }
                } catch (e: Throwable) {
                    transfer.rollback()
                    throw e
                }

                usingFallbackStorageMutable.value = false
                refreshVideosState()
                // The move already worked, so leftovers are just wasted space until the next merge
                transfer.commit().forEach { Logger.w("Could not delete old video $it") }
            }.onFailure { Logger.w("Failed to move videos to $location", it) }
        }

    /** Moves videos that are in [from] but not in [into], then deletes them from [from] */
    private fun mergeInto(into: File, from: File) {
        VideoFolderTransfer.deleteTemporaryFiles(from)
        if (from.listFiles().isNullOrEmpty()) return
        val transfer = VideoFolderTransfer.copy(from = from, to = into, failOnConflict = false)
        transfer.commit().forEach { Logger.w("Could not delete merged video $it") }
    }

    private fun switchVideosDir(dir: File) {
        synchronized(filesLock) {
            dir.mkdirs()
            videosDir = dir
        }
        refreshVideosState()
    }

    fun deleteVideo(date: LocalDate) {
        synchronized(filesLock) {
            val file = getVideoFile(date)
            file.delete()
            getThumbnailFile(date).delete()
        }
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
            Logger.e("Error discarding video", e)
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

                val destinationFile = synchronized(filesLock) {
                    getVideoFile(date).also { destinationFile ->
                        destinationFile.parentFile?.mkdirs()
                        if (destinationFile.exists()) {
                            destinationFile.delete()
                        }
                        sourceFile.copyTo(destinationFile)
                    }
                }
                videoThumbnailExtractor.extractFirstFrame(destinationFile, getThumbnailFile(date))
                refreshVideosState()
            } else {
                Logger.e("Failed to get video path from content URI")
            }

            cursor?.close()
        } catch (e: Exception) {
            Logger.e("Error copying video", e)
        }
    }

    /**
     * Deletes all existing videos and creates placeholder video files for [dates], copied from
     * the `sample_video` raw resource. Used to fill the diary with mock data for debugging.
     */
    fun fillWithMockVideos(dates: List<LocalDate>) {
        synchronized(filesLock) {
            videosDir.listFiles()?.forEach { it.delete() }
            thumbnailsDir.listFiles()?.forEach { it.delete() }
            dates.forEach { date ->
                val videoFile = getVideoFile(date)
                context.resources.openRawResource(R.raw.sample_video).use { input ->
                    videoFile.outputStream().use { output -> input.copyTo(output) }
                }
                videoThumbnailExtractor.extractFirstFrame(videoFile, getThumbnailFile(date))
            }
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
        val videoFiles = videosDir.listFiles()
            ?.filterNot { it.name.endsWith(VideoFolderTransfer.TEMPORARY_SUFFIX) }
            ?: return
        var generatedAny = false
        videoFiles.forEach { videoFile ->
            val date = try {
                videoFileNameMapper.nameToDate(videoFile.name)
            } catch (e: Exception) {
                Logger.w("Skipping video with unrecognised file name: ${videoFile.name}", e)
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
        VideoStorageLocation.Internal -> internalVideosDir
        VideoStorageLocation.RemovableStorage -> context.getExternalFilesDirs(null)
            .firstOrNull { directory ->
                directory != null &&
                    Environment.isExternalStorageRemovable(directory) &&
                    Environment.getExternalStorageState(directory) == Environment.MEDIA_MOUNTED
            }
            ?.let { File(it, "videos") }
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
        return files.filterNot { it.name.endsWith(VideoFolderTransfer.TEMPORARY_SUFFIX) }
    }
}