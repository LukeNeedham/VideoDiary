package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import com.lukeneedham.videodiary.domain.model.ExportedVideo
import java.io.File
import java.time.LocalDate

object MockDataExportDiaryView {
    val file = File("mock")
    val startDate = LocalDate.of(2024, 3, 27)
    val endDate = LocalDate.of(2024, 4, 20)
    val exportedVideo = ExportedVideo(
        videoFile = file,
        startDate = startDate,
        endDate = endDate,
        dayVideoCount = 10,
    )
}