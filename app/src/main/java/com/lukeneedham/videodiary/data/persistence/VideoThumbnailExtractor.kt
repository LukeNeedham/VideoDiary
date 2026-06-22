package com.lukeneedham.videodiary.data.persistence

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.media.MediaMetadataRetriever
import com.lukeneedham.videodiary.domain.util.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Extracts the first frame of a video file and saves it as a JPEG thumbnail.
 *
 * [MediaMetadataRetriever] decodes frames using BT.601 YUV-to-RGB conversion, but
 * HD video (recorded by CameraX) is encoded as BT.709 and played back using BT.709
 * by ExoPlayer. A colour-correction matrix is applied so the saved thumbnail matches
 * what the video looks like during playback.
 */
class VideoThumbnailExtractor(
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun extractFirstFrame(videoFile: File, thumbnailFile: File) {
        withContext(ioDispatcher) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(videoFile.absolutePath)
                val frame = retriever.frameAtTime ?: return@withContext
                val corrected = applyBt709Correction(frame)
                FileOutputStream(thumbnailFile).use { output ->
                    corrected.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
                }
            } catch (e: Exception) {
                Logger.error("Error extracting video thumbnail", e)
            } finally {
                retriever.release()
            }
        }
    }

    companion object {
        private const val JPEG_QUALITY = 90

        // Combined matrix: inverse(BT.601 limited-range) * BT.709 limited-range.
        // Converts RGB values produced by a BT.601 decode back to YCbCr, then
        // re-decodes them using BT.709 coefficients to match on-screen playback.
        private val BT601_TO_BT709 = ColorMatrix(
            floatArrayOf(
                1.0863f, -0.0724f, -0.0140f, 0f, 0f,
                0.0963f, 0.8456f, 0.0583f, 0f, 0f,
                -0.0143f, -0.0280f, 1.0423f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )

        private val correctionPaint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(BT601_TO_BT709)
        }

        private fun applyBt709Correction(src: Bitmap): Bitmap {
            val dst = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
            Canvas(dst).drawBitmap(src, 0f, 0f, correctionPaint)
            return dst
        }
    }
}
