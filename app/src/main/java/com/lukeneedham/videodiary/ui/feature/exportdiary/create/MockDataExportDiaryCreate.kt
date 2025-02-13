package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportState
import java.time.LocalDate

object MockDataExportDiaryCreate {
    val exportState = ExportState.Ready
    val diaryStartDate = LocalDate.of(2024, 3, 25)
    val startDate = LocalDate.of(2024, 3, 27)
    val endDate = LocalDate.of(2024, 4, 20)
}