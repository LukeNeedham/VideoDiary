package com.lukeneedham.videodiary.ui.feature.recap.export

import com.lukeneedham.videodiary.ui.feature.recap.export.model.RecapExportProgressState

object MockDataRecapExportProgress {
    val inProgress = RecapExportProgressState.InProgress(0.4f)
    val failed = RecapExportProgressState.Failed("Something broke")
}
