package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.transformer.ExperimentalFrameExtractor
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.lukeneedham.videodiary.domain.util.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Extracts the first frame of a video file and saves it as a JPEG thumbnail.
 *
 * Frames are extracted with [ExperimentalFrameExtractor], which decodes and renders frames
 * through the same decoder/colour pipeline used for video playback, so the thumbnail's
 * colours match what the video looks like once playback starts.
 */
@OptIn(UnstableApi::class)
class VideoThumbnailExtractor(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun extractFirstFrame(videoFile: File, thumbnailFile: File) {
        val frameExtractor = ExperimentalFrameExtractor(
            context,
            ExperimentalFrameExtractor.Configuration.Builder()
                // Match the hardware decoders used for on-screen playback, so the
                // extracted frame's colours match the video's.
                .setMediaCodecSelector(MediaCodecSelector.DEFAULT)
                .build(),
        )
        try {
            frameExtractor.setMediaItem(MediaItem.fromUri(videoFile.toUri()), emptyList())
            val frame = frameExtractor.getFrame(0L).await()
            withContext(ioDispatcher) {
                FileOutputStream(thumbnailFile).use { output ->
                    frame.bitmap.compress(Bitmap.CompressFormat.JPEG, jpegQuality, output)
                }
            }
        } catch (e: Exception) {
            Logger.error("Error extracting video thumbnail", e)
        } finally {
            frameExtractor.release()
        }
    }

    companion object {
        private const val jpegQuality = 90
    }
}

private suspend fun <T> ListenableFuture<T>.await(): T =
    suspendCancellableCoroutine { continuation ->
        Futures.addCallback(
            this,
            object : FutureCallback<T> {
                override fun onSuccess(result: T) = continuation.resume(result)
                override fun onFailure(t: Throwable) = continuation.resumeWithException(t)
            },
            MoreExecutors.directExecutor(),
        )
        continuation.invokeOnCancellation { cancel(false) }
    }
