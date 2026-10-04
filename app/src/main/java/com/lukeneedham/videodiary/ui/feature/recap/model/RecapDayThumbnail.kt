package com.lukeneedham.videodiary.ui.feature.recap.model

import java.io.File
import java.time.LocalDate

data class RecapDayThumbnail(
    val date: LocalDate,
    val thumbnailFile: File?,
)
