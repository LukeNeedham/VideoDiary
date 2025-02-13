package com.lukeneedham.videodiary.data.persistence.export

import android.content.Context
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import androidx.annotation.OptIn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.OverlaySettings
import androidx.media3.effect.TextOverlay
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
import com.lukeneedham.videodiary.util.ext.setFullLengthSpan
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
    ): Flow<VideoExportState> {
        // Delete existing export if it exists -
        // there should be at most 1 export file at any time, to avoid clutter
        outputFile.delete()

        isComplete = false
        failureException = null

        val editedMediaItems = inputVideos.map { input ->
            val mediaItem = MediaItem.fromUri(input.video.toUri())

            val date = input.date
            val time = input.time
            val dateOverlay = createTextOverlay(
                text = date.format(StandardDateTimeFormatter.date),
                foregroundColor = Color.White,
                backgroundColor = Color.Black.copy(alpha = 0.5f),
                textSize = 60,
                x = 0f,
                y = -0.8f,
            )
            val timeOverlay = createTextOverlay(
                text = time.format(StandardDateTimeFormatter.time),
                foregroundColor = Color.White,
                backgroundColor = Color.Black.copy(alpha = 0.5f),
                textSize = 40,
                x = 0f,
                y = 0.9f,
            )

            val effects = listOf(dateOverlay, timeOverlay)
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
        foregroundColor: Color,
        backgroundColor: Color,
        textSize: Int,
        x: Float,
        y: Float,
    ): TextOverlay {
        val overlaySettings = OverlaySettings.Builder()
        overlaySettings.setOverlayFrameAnchor(1f, -1f)
        overlaySettings.setBackgroundFrameAnchor(x, -y)

        val spanString = SpannableString(text)
        spanString.apply {
            setFullLengthSpan(ForegroundColorSpan(foregroundColor.toArgb()))
            setFullLengthSpan(BackgroundColorSpan(backgroundColor.toArgb()))
            setFullLengthSpan(AbsoluteSizeSpan(textSize))
        }
        return TextOverlay.createStaticTextOverlay(
            spanString,
            overlaySettings.build()
        )
    }
}