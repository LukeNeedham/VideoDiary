package com.lukeneedham.videodiary.ui.feature.recap.hub

import com.lukeneedham.videodiary.domain.model.SavedRecap
import java.io.File

data class SavedRecapWithThumbnails(
    val recap: SavedRecap,
    val dayVideoCount: Int,
    val thumbnailFiles: List<File>,
)
