package com.lukeneedham.videodiary.domain.util.date

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

object CalendarUtil {
    /** @return an ordered list of all dates between [startDate] and [endDate] (both inclusive) */
    fun getAllDates(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        var date = startDate
        val allDates = mutableListOf<LocalDate>()
        while (date <= endDate) {
            allDates.add(date)
            date = date.plusDays(1)
        }
        return allDates
    }

    fun <HasDate> chunkIntoWeeks(
        items: List<HasDate>,
        getDate: (HasDate) -> LocalDate
    ): List<List<HasDate>> {
        val weekFields = WeekFields.of(DayOfWeek.MONDAY, 1)

        val firstDay = items.firstOrNull() ?: return emptyList()
        val firstDate = getDate(firstDay)

        fun getWeekNum(date: LocalDate) = date.get(weekFields.weekOfWeekBasedYear())

        val weeks = mutableListOf<List<HasDate>>()

        var currentWeekNum: Int = getWeekNum(firstDate)
        val currentWeekDates = mutableListOf<HasDate>()

        items.forEach { item ->
            val date = getDate(item)
            val weekNumber = getWeekNum(date)
            if (weekNumber == currentWeekNum) {
                currentWeekDates.add(item)
            } else {
                weeks.add(currentWeekDates.toList())
                currentWeekDates.clear()
                currentWeekDates.add(item)
                currentWeekNum = weekNumber
            }
        }
        if (currentWeekDates.isNotEmpty()) {
            weeks.add(currentWeekDates)
        }

        return weeks
    }
}