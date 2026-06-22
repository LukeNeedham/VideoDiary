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
 * The saved thumbnail is colour-corrected so it visually matches ExoPlayer
 * playback. Two adjustments are combined into a single [ColorMatrix]:
 *
 * 1. **BT.601 → BT.709** — [MediaMetadataRetriever] decodes with BT.601
 *    coefficients, but CameraX records BT.709 video and ExoPlayer plays it
 *    back as BT.709.
 * 2. **Saturation boost** — the Compose [Image] rendering path (software
 *    bitmap → sRGB 2D canvas) produces slightly less vivid colours than
 *    ExoPlayer's hardware-accelerated TextureView pipeline. A small
 *    saturation increase compensates for this difference.
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
                val corrected = applyColourCorrection(frame)
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
        private const val SATURATION_BOOST = 1.2f

        // BT.601→BT.709 colour-space correction matrix.
        private val BT601_TO_BT709 = ColorMatrix(
            floatArrayOf(
                1.0863f, -0.0724f, -0.0140f, 0f, 0f,
                0.0963f, 0.8456f, 0.0583f, 0f, 0f,
                -0.0143f, -0.0280f, 1.0423f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )

        private val correctionMatrix = ColorMatrix().apply {
            set(BT601_TO_BT709)
            val saturationMatrix = ColorMatrix()
            saturationMatrix.setSaturation(SATURATION_BOOST)
            postConcat(saturationMatrix)
        }

        private val correctionPaint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(correctionMatrix)
        }

        private fun applyColourCorrection(src: Bitmap): Bitmap {
            val dst = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
            Canvas(dst).drawBitmap(src, 0f, 0f, correctionPaint)
            return dst
        }
    }
}
