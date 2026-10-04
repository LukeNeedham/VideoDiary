package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.data.persistence.export.VideoExporter
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDay
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportOptions
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.time.LocalDate

class VideoExportDao(
    context: Context,
    private val videoExporter: VideoExporter,
) {
    private val outputDir = File(context.filesDir, "export").apply {
        mkdirs()
    }

    /**
     * The already-exported file for this exact recap + [options], if one exists - so a recap
     * doesn't need to be re-exported just because the user left and came back to share it.
     */
    fun getExistingExport(startDate: LocalDate, endDate: LocalDate, options: RecapExportOptions): File? {
        val file = outputFile(startDate, endDate, options)
        return file.takeIf { it.exists() }
    }

    fun export(
        videos: List<RecapDay>,
        startDate: LocalDate,
        endDate: LocalDate,
        options: RecapExportOptions,
    ): Flow<VideoExportState> =
        videoExporter.export(videos, outputFile(startDate, endDate, options), options)

    fun cancel() = videoExporter.cancel()

    // One file per (date range, options): each distinct combination of export options for the
    // same recap is exported and cached independently, rather than one overwriting another.
    private fun outputFile(startDate: LocalDate, endDate: LocalDate, options: RecapExportOptions): File =
        File(outputDir, "recap_${startDate}_${endDate}_${options.cacheKey}.mp4")
}
