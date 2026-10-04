package com.lukeneedham.videodiary.ui.feature.recap.share

import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import java.io.File

object MockDataRecapShare {
    val inProgress = RecapShareState.InProgress(0.4f)
    val ready = RecapShareState.Ready(File("mock-output.mp4"))
    val failed = RecapShareState.Failed("Something broke")
}
