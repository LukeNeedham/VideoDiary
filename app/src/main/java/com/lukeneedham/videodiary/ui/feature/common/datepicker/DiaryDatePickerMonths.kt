package com.lukeneedham.videodiary.ui.feature.common.datepicker

import com.lukeneedham.videodiary.domain.model.Day
import java.time.YearMonth

/** All days of the diary, grouped by calendar month in chronological order */
fun List<Day>.groupedByMonth(): List<List<Day>> =
    groupBy { YearMonth.from(it.date) }
        .toSortedMap()
        .values
        .toList()
