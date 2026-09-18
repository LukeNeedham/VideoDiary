package com.lukeneedham.videodiary.ui.feature.recap.create

import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Builds the list of whole months/weeks/years the user can jump straight into a recap of,
 * most-recent-first, skipping any period with no recorded videos at all.
 *
 * These are purely a UI convenience for picking a date range - the resulting [RecapPeriodOption]
 * only carries a suggested name and a date range, and nothing about "this was a month/week/year
 * recap" is ever persisted.
 */
object RecapPeriodOptions {
    private val weekFields = WeekFields.of(DayOfWeek.MONDAY, 1)

    fun build(days: List<Day>, periodType: RecapPeriodType): List<RecapPeriodOption> {
        val diaryStart = days.firstOrNull()?.date ?: return emptyList()
        val today = days.lastOrNull()?.date ?: return emptyList()
        val recordedDates = days.filter { it.videoFile != null }.map { it.date }
        if (recordedDates.isEmpty()) return emptyList()

        val options = when (periodType) {
            RecapPeriodType.MONTH -> buildMonths(diaryStart, today)
            RecapPeriodType.WEEK -> buildWeeks(diaryStart, today)
            RecapPeriodType.YEAR -> buildYears(diaryStart, today)
        }

        return options
            .filter { option -> recordedDates.any { it in option.startDate..option.endDate } }
            .reversed()
    }

    private fun buildMonths(diaryStart: LocalDate, today: LocalDate): List<RecapPeriodOption> {
        val options = mutableListOf<RecapPeriodOption>()
        var yearMonth = YearMonth.from(diaryStart)
        val lastYearMonth = YearMonth.from(today)
        while (!yearMonth.isAfter(lastYearMonth)) {
            val startDate = maxOf(yearMonth.atDay(1), diaryStart)
            val endDate = minOf(yearMonth.atEndOfMonth(), today)
            val monthName = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val label = "$monthName ${yearMonth.year}"
            options.add(
                RecapPeriodOption(
                    label = label,
                    startDate = startDate,
                    endDate = endDate,
                    suggestedName = "$label recap",
                )
            )
            yearMonth = yearMonth.plusMonths(1)
        }
        return options
    }

    private fun buildWeeks(diaryStart: LocalDate, today: LocalDate): List<RecapPeriodOption> {
        val options = mutableListOf<RecapPeriodOption>()
        var weekStart = diaryStart.with(DayOfWeek.MONDAY)
        val lastWeekStart = today.with(DayOfWeek.MONDAY)
        while (!weekStart.isAfter(lastWeekStart)) {
            val weekEnd = weekStart.plusDays(6)
            val startDate = maxOf(weekStart, diaryStart)
            val endDate = minOf(weekEnd, today)
            val weekNumber = weekStart.get(weekFields.weekOfWeekBasedYear())
            val label = "Week $weekNumber"
            options.add(
                RecapPeriodOption(
                    label = label,
                    startDate = startDate,
                    endDate = endDate,
                    suggestedName = "$label recap",
                )
            )
            weekStart = weekStart.plusWeeks(1)
        }
        return options
    }

    private fun buildYears(diaryStart: LocalDate, today: LocalDate): List<RecapPeriodOption> {
        val options = mutableListOf<RecapPeriodOption>()
        var year = Year.from(diaryStart)
        val lastYear = Year.from(today)
        while (!year.isAfter(lastYear)) {
            val startDate = maxOf(year.atDay(1), diaryStart)
            val endDate = minOf(year.atMonth(12).atEndOfMonth(), today)
            val label = "${year.value}"
            options.add(
                RecapPeriodOption(
                    label = label,
                    startDate = startDate,
                    endDate = endDate,
                    suggestedName = "$label recap",
                )
            )
            year = year.plusYears(1)
        }
        return options
    }
}
