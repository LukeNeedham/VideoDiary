package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.data.persistence.export.VideoExporter
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDay
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
     * The already-exported file for this exact recap + date-stamp choice, if one exists - so a
     * recap doesn't need to be re-exported just because the user left and came back to share it.
     */
    fun getExistingExport(startDate: LocalDate, endDate: LocalDate, includeDateStamp: Boolean): File? {
        val file = outputFile(startDate, endDate, includeDateStamp)
        return file.takeIf { it.exists() }
    }

    fun export(
        videos: List<RecapDay>,
        startDate: LocalDate,
        endDate: LocalDate,
        exportIncludeDateStamp: Boolean,
    ): Flow<VideoExportState> =
        videoExporter.export(videos, outputFile(startDate, endDate, exportIncludeDateStamp), exportIncludeDateStamp)

    fun cancel() = videoExporter.cancel()

    // One file per (date range, date-stamp choice): the two choices for the same recap are
    // exported and cached independently, rather than one overwriting the other.
    private fun outputFile(startDate: LocalDate, endDate: LocalDate, includeDateStamp: Boolean): File {
        val dateStampSuffix = if (includeDateStamp) "stamped" else "plain"
        return File(outputDir, "recap_${startDate}_${endDate}_$dateStampSuffix.mp4")
    }
}
