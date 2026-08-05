package com.lukeneedham.videodiary.ui.feature.exportdiary.progress

import com.lukeneedham.videodiary.ui.feature.exportdiary.progress.model.ExportProgressState

object MockDataExportDiaryProgress {
    val inProgress = ExportProgressState.InProgress(0.4f)
    val failed = ExportProgressState.Failed("Something broke")
}
