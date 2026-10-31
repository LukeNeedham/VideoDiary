package com.lukeneedham.videodiary.ui.feature.calendar

import com.lukeneedham.videodiary.domain.model.Day

/**
 * The index to show once the days (this list) change to [newDays]. The list can grow or shrink at the start
 * when videos appear or disappear (for example when the storage location changes), so the index
 * alone is not enough: stay on the same date if it still exists, otherwise go to the last day.
 */
internal fun List<Day>.resolveCurrentDayIndex(
    oldIndex: Int,
    newDays: List<Day>,
): Int {
    val currentDate = getOrNull(oldIndex)?.date
    val newIndex = newDays.indexOfFirst { it.date == currentDate }
    return if (newIndex != -1) newIndex else newDays.lastIndex.coerceAtLeast(0)
}
