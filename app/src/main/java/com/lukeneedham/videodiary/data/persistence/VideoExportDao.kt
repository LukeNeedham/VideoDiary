package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.data.persistence.export.VideoExporter
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDay
import kotlinx.coroutines.flow.Flow
import java.io.File

class VideoExportDao(
    context: Context,
    private val videoExporter: VideoExporter,
) {
    private val outputDir = File(context.filesDir, "export").apply {
        mkdirs()
    }
    private val outputFile = File(outputDir, outputFileName)

    fun export(videos: List<ExportDay>): Flow<VideoExportState> =
        videoExporter.export(videos, outputFile)

    companion object {
        const val outputFileName = "export.mp4"
    }
}