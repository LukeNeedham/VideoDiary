package com.lukeneedham.videodiary.data.persistence

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
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
            val correctedFrame = matchVideoPlaybackColors(frame)
            FileOutputStream(thumbnailFile).use { output ->
                correctedFrame.compress(Bitmap.CompressFormat.JPEG, jpegQuality, output)
            }
        } catch (e: Exception) {
            Logger.error("Error extracting video thumbnail", e)
        } finally {
            retriever.release()
        }
    }

    /**
     * [MediaMetadataRetriever] decodes frames using BT.601 YUV->RGB conversion, but
     * recorded videos are encoded as BT.709 (the standard for HD video) and are
     * rendered using BT.709 during playback. Without correction, this makes the
     * extracted frame noticeably less saturated than the video, causing a visible
     * colour shift when playback starts. Re-mapping the colours from BT.601 to
     * BT.709 here makes the thumbnail match what the video will look like.
     */
    private fun matchVideoPlaybackColors(frame: Bitmap): Bitmap {
        val corrected = Bitmap.createBitmap(
            frame.width,
            frame.height,
            frame.config ?: Bitmap.Config.ARGB_8888,
        )
        Canvas(corrected).drawBitmap(
            frame,
            0f,
            0f,
            Paint().apply { colorFilter = ColorMatrixColorFilter(bt601ToBt709) },
        )
        return corrected
    }

    companion object {
        private const val jpegQuality = 90

        // Converts BT.601-decoded RGB back to YCbCr and re-decodes it using
        // BT.709 coefficients, i.e. bt709Matrix * inverse(bt601Matrix).
        private val bt601ToBt709 = ColorMatrix(
            floatArrayOf(
                1.0863f, -0.0724f, -0.0140f, 0f, 0f,
                0.0963f, 0.8456f, 0.0583f, 0f, 0f,
                -0.0143f, -0.0280f, 1.0423f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )
    }
}
