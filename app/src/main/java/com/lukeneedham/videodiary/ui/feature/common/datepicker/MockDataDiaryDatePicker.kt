package com.lukeneedham.videodiary.ui.feature.common.datepicker

import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.util.date.CalendarUtil
import java.time.LocalDate

object MockDataDiaryDatePicker {
    val startDate = LocalDate.of(2024, 8, 4)
    val endDate = LocalDate.of(2025, 2, 11)
    val day = Day(startDate, null)

    val days = CalendarUtil.getAllDates(
        startDate = startDate,
        endDate = endDate,
    ).map {
        Day(
            date = it, video = null,
        )
    }
    val weeks = CalendarUtil.chunkIntoWeeks(days) { it.date }
}