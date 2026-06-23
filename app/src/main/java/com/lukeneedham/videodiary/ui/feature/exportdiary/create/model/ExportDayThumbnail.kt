package com.lukeneedham.videodiary.ui.feature.exportdiary.create.model

import java.io.File
import java.time.LocalDate

data class ExportDayThumbnail(
    val date: LocalDate,
    val thumbnailFile: File?,
)
