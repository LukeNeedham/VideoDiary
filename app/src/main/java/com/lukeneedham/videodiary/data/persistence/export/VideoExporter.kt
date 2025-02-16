package com.lukeneedham.videodiary.data.persistence.export

import android.content.Context
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.OverlaySettings
import androidx.media3.effect.TextureOverlay
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.Effects
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDay
import com.lukeneedham.videodiary.util.ext.toOverlayEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

@OptIn(UnstableApi::class)
class VideoExporter(
    context: Context,
) {
    private var isComplete = false
    private var failureException: Exception? = null

    private val listener = object : Transformer.Listener {
        override fun onCompleted(composition: Composition, exportResult: ExportResult) {
            super.onCompleted(composition, exportResult)
            isComplete = true
        }

        override fun onError(
            composition: Composition,
            exportResult: ExportResult,
            exportException: ExportException
        ) {
            super.onError(composition, exportResult, exportException)
            failureException = exportException
        }
    }

    private val transformer = Transformer.Builder(context)
        .addListener(listener)
        .build()

    @OptIn(UnstableApi::class)
    fun export(
        inputVideos: List<ExportDay>,
        outputFile: File,
        exportIncludeDateStamp: Boolean,
    ): Flow<VideoExportState> {
        // Delete existing export if it exists -
        // there should be at most 1 export file at any time, to avoid clutter
        outputFile.delete()

        isComplete = false
        failureException = null

        val editedMediaItems = inputVideos.map { input ->
            val mediaItem = MediaItem.fromUri(input.video.toUri())

            val date = input.date
            val dateOverlay = if (exportIncludeDateStamp) {
                createTextOverlay(text = date.format(StandardDateTimeFormatter.date))
            } else {
                null
            }

            val effects = listOfNotNull(dateOverlay)
                .map { it.toOverlayEffect() }

            EditedMediaItem.Builder(mediaItem).apply {
                setEffects(Effects(emptyList(), effects))
            }.build()
        }

        val composition = Composition.Builder(
            EditedMediaItemSequence.Builder(editedMediaItems).build()
        ).build()

        transformer.start(
            composition,
            outputFile.absolutePath,
        )

        val progressHolder = ProgressHolder()
        var isFlowing = true
        return flow {
            while (isFlowing) {
                val error = failureException
                val state = when {
                    isComplete -> {
                        isFlowing = false
                        VideoExportState.Success(outputFile)
                    }

                    error != null -> {
                        isFlowing = false
                        VideoExportState.Failure(error)
                    }

                    else -> {
                        transformer.getProgress(progressHolder)
                        val progressFraction = progressHolder.progress / 100f
                        VideoExportState.InProgress(progressFraction)
                    }
                }
                emit(state)
                delay(250)
            }
        }
    }

    private fun createTextOverlay(
        text: String,
    ): TextureOverlay {
        /**
         * Let's say you have a small logo image as your overlay.
         * You call setOverlayFrameAnchor(1, 1) - You've chosen the top-right corner of your logo as the anchor.
         * You call setBackgroundFrameAnchor(-1, -1) - You want to place the logo in the bottom-left corner of the video.
         * The result will be that the top-right corner of your logo will be positioned at the bottom-left corner of the video frame.
         */
        val overlaySettings = OverlaySettings.Builder().apply {
            setOverlayFrameAnchor(0f, -1f)
            setBackgroundFrameAnchor(0f, -1f)
        }.build()

        return DateOverlay(
            text = text,
            paddingBottom = 30,
            overlaySettings = overlaySettings,
        )
    }
}