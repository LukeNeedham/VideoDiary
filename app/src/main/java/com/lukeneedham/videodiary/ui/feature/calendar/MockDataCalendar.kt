package com.lukeneedham.videodiary.ui.feature.calendar

import com.lukeneedham.videodiary.domain.model.Day
import java.io.File
import java.time.LocalDate

object MockDataCalendar {
    val startDate = LocalDate.of(2024, 3, 27)

    val file = File("")
    val day = Day(
        date = LocalDate.of(2024, 12, 27), video = file,
    )
    val days = listOf(
        day
    )

    val videoAspectRatio = 1f
}