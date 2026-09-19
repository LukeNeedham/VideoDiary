package com.lukeneedham.videodiary.ui.feature.recap.create

import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
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
 * oldest-first. A period that hasn't fully elapsed yet (e.g. the current month) is left out
 * entirely - e.g. September isn't offered until the 1st of October. A period that has fully
 * elapsed but has no recorded videos is still included (with [RecapPeriodOption.videoCount] of
 * 0) so the UI can show it as an unselectable, greyed-out option.
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

        return when (periodType) {
            RecapPeriodType.MONTH -> buildMonths(diaryStart, today, recordedDates)
            RecapPeriodType.WEEK -> buildWeeks(diaryStart, today, recordedDates)
            RecapPeriodType.YEAR -> buildYears(diaryStart, today, recordedDates)
        }
    }

    private fun buildMonths(
        diaryStart: LocalDate,
        today: LocalDate,
        recordedDates: List<LocalDate>,
    ): List<RecapPeriodOption> {
        val options = mutableListOf<RecapPeriodOption>()
        var yearMonth = YearMonth.from(diaryStart)
        while (true) {
            val naturalEnd = yearMonth.atEndOfMonth()
            if (!naturalEnd.isBefore(today)) break

            val startDate = maxOf(yearMonth.atDay(1), diaryStart)
            val monthName = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val label = if (yearMonth.year == today.year) {
                monthName
            } else {
                "$monthName ${yearMonth.year}"
            }
            options.add(
                RecapPeriodOption(
                    label = label,
                    startDate = startDate,
                    endDate = naturalEnd,
                    dateRangeText = formatDateRange(startDate, naturalEnd),
                    suggestedName = "$label recap",
                    videoCount = recordedDates.count { it in startDate..naturalEnd },
                )
            )
            yearMonth = yearMonth.plusMonths(1)
        }
        return options
    }

    private fun buildWeeks(
        diaryStart: LocalDate,
        today: LocalDate,
        recordedDates: List<LocalDate>,
    ): List<RecapPeriodOption> {
        val options = mutableListOf<RecapPeriodOption>()
        val currentWeekYear = today.get(weekFields.weekBasedYear())
        var weekStart = diaryStart.with(DayOfWeek.MONDAY)
        while (true) {
            val naturalEnd = weekStart.plusDays(6)
            if (!naturalEnd.isBefore(today)) break

            val startDate = maxOf(weekStart, diaryStart)
            val weekNumber = weekStart.get(weekFields.weekOfWeekBasedYear())
            val weekYear = weekStart.get(weekFields.weekBasedYear())
            val label = if (weekYear == currentWeekYear) {
                "Week $weekNumber"
            } else {
                "Week $weekNumber, $weekYear"
            }
            options.add(
                RecapPeriodOption(
                    label = label,
                    startDate = startDate,
                    endDate = naturalEnd,
                    dateRangeText = formatDateRange(startDate, naturalEnd),
                    suggestedName = "$label recap",
                    videoCount = recordedDates.count { it in startDate..naturalEnd },
                )
            )
            weekStart = weekStart.plusWeeks(1)
        }
        return options
    }

    private fun buildYears(
        diaryStart: LocalDate,
        today: LocalDate,
        recordedDates: List<LocalDate>,
    ): List<RecapPeriodOption> {
        val options = mutableListOf<RecapPeriodOption>()
        var year = Year.from(diaryStart)
        while (true) {
            val naturalEnd = year.atMonth(12).atEndOfMonth()
            if (!naturalEnd.isBefore(today)) break

            val startDate = maxOf(year.atDay(1), diaryStart)
            val label = "${year.value}"
            options.add(
                RecapPeriodOption(
                    label = label,
                    startDate = startDate,
                    endDate = naturalEnd,
                    dateRangeText = formatDateRange(startDate, naturalEnd),
                    suggestedName = "$label recap",
                    videoCount = recordedDates.count { it in startDate..naturalEnd },
                )
            )
            year = year.plusYears(1)
        }
        return options
    }

    /**
     * A compact rendering of a date range, omitting whatever's implied by the other date -
     * e.g. "10 - 30 September" (year and month both dropped from the start date since they're
     * shared with the end date), or "29 Dec - 4 Jan" / "29 Dec 2025 - 4 Jan 2026" as the shared
     * parts narrow.
     */
    private fun formatDateRange(startDate: LocalDate, endDate: LocalDate): String {
        val startDay = startDate.format(StandardDateTimeFormatter.dayOfMonth)
        val endDay = endDate.format(StandardDateTimeFormatter.dayOfMonth)
        return when {
            startDate.year == endDate.year && startDate.month == endDate.month -> {
                val month = endDate.format(StandardDateTimeFormatter.monthFull)
                "$startDay - $endDay $month"
            }

            startDate.year == endDate.year -> {
                val startMonth = startDate.format(StandardDateTimeFormatter.monthShort)
                val endMonth = endDate.format(StandardDateTimeFormatter.monthShort)
                "$startDay $startMonth - $endDay $endMonth"
            }

            else -> {
                val startMonth = startDate.format(StandardDateTimeFormatter.monthShort)
                val endMonth = endDate.format(StandardDateTimeFormatter.monthShort)
                "$startDay $startMonth ${startDate.year} - $endDay $endMonth ${endDate.year}"
            }
        }
    }
}
