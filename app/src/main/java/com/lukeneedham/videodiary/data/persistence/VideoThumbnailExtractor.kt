package com.lukeneedham.videodiary.data.persistence

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.lukeneedham.videodiary.domain.util.logger.Logger
import java.io.File
import java.io.FileOutputStream

/**
 * Extracts the first frame of a video file and saves it as a JPEG thumbnail.
 */
class VideoThumbnailExtractor {
    fun extractFirstFrame(videoFile: File, thumbnailFile: File) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoFile.absolutePath)
            val frame = retriever.frameAtTime ?: return
            FileOutputStream(thumbnailFile).use { output ->
                frame.compress(Bitmap.CompressFormat.JPEG, jpegQuality, output)
            }
        } catch (e: Exception) {
            Logger.error("Error extracting video thumbnail", e)
        } finally {
            retriever.release()
        }
    }

    companion object {
        private const val jpegQuality = 90
    }
}
