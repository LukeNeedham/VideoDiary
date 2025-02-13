package com.lukeneedham.videodiary.ui.feature.exportdiary.create.model

import java.io.File
import java.time.LocalDate
import java.time.LocalTime

data class ExportDay(
    val date: LocalDate,
    val time: LocalTime,
    val video: File,
)