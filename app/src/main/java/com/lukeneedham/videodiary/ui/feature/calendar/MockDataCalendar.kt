package com.lukeneedham.videodiary.ui.feature.calendar

import com.lukeneedham.videodiary.domain.model.Day
import java.io.File
import java.time.LocalDate

object MockDataCalendar {
    val startDate = LocalDate.of(2024, 3, 27)

    val file = File("")
    val dayWithVideo = Day(
        date = LocalDate.of(2024, 12, 27),
        videoFile = file,
    )
    val dayWithoutVideo = Day(
        date = LocalDate.now(),
        videoFile = null,
    )
    val days = listOf(
        dayWithVideo,
        dayWithoutVideo,
    )

    val videoAspectRatio = 1f
}