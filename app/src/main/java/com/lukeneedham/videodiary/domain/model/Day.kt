package com.lukeneedham.videodiary.domain.model

import java.io.File
import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val videoFile: File?,
    val thumbnailFile: File? = null,
) {
    val today = LocalDate.now()
    val isToday = today == date
    val video = videoFile?.let { Video.PersistedFile(it) }
}