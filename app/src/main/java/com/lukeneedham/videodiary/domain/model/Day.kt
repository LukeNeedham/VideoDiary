package com.lukeneedham.videodiary.domain.model

import java.io.File
import java.time.LocalDate
import java.time.LocalTime

data class Day(val date: LocalDate, val video: File?) {
    val today = LocalDate.now()
    val isToday = today == date
}