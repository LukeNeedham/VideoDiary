package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.videodiary.domain.util.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate

class VideosDao(
    private val context: Context,
    private val videoFileNameMapper: VideoFileNameMapper,
) {
    private val videosDir = File(context.filesDir, "videos").apply {
        mkdirs()
    }

    // todo: wire this up to store and use thumbnails
    private val thumbnailsDir = File(context.filesDir, "thumbnails").apply {
        mkdirs()
    }

    private val allVideosMutable = MutableStateFlow(loadAllVideos())

    /** A Flow of all video files in the diary, unordered */
    val allVideos = allVideosMutable.asStateFlow()

    fun deleteVideo(date: LocalDate) {
        val file = getVideoFile(date)
        file.delete()
        refreshVideosState()
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
     * Deletes all existing videos and creates empty placeholder video files for [dates].
     * Used to fill the diary with mock data for debugging.
     */
    fun fillWithMockVideos(dates: List<LocalDate>) {
        videosDir.listFiles()?.forEach { it.delete() }
        dates.forEach { date ->
            getVideoFile(date).createNewFile()
        }
        refreshVideosState()
    }

    fun getVideoFile(date: LocalDate) = getVideoFile(getVideoFileName(date))

    fun getVideoFileIfExists(date: LocalDate): File? {
        val file = getVideoFile(getVideoFileName(date))
        return if (file.exists()) file else null
    }

    fun getThumbnailFileIfExists(date: LocalDate): File? {
        // todo: no thumbnails yet - implement with thumbnailsDir
        return null
    }

    private fun getVideoFile(name: String) = File(videosDir, name)

    private fun getVideoFileName(date: LocalDate) = videoFileNameMapper.dateToName(date)

    /** Should be invoked whenever the persisted video files change in any way */
    private fun refreshVideosState() {
        allVideosMutable.value = loadAllVideos()
    }

    private fun loadAllVideos(): List<File> {
        val files = videosDir.listFiles() ?: return emptyList()
        return files.toList()
    }
}