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

    fun getVideoFile(date: LocalDate) = getVideoFile(getVideoFileName(date))

    private fun getVideoFile(name: String) = File(videosDir, name)

    private fun getVideoFileName(date: LocalDate) = videoFileNameMapper.dateToName(date) + ".mp4"

    /** Should be invoked whenever the persisted video files change in any way */
    private fun refreshVideosState() {
        allVideosMutable.value = loadAllVideos()
    }

    private fun loadAllVideos(): List<File> {
        val files = videosDir.listFiles() ?: return emptyList()
        return files.toList()
    }
}