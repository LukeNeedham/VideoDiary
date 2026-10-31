package com.lukeneedham.videodiary.ui.feature.calendar

import com.lukeneedham.videodiary.domain.model.Day
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class CalendarDayIndexTest {
    private val today = LocalDate.of(2026, 10, 25)

    /** [count] days ending today */
    private fun days(count: Int) = (count - 1 downTo 0).map {
        Day(date = today.minusDays(it.toLong()), videoFile = null, thumbnailFile = null)
    }

    @Test
    fun firstLoadGoesToLastDay() {
        assertEquals(59, CalendarDayIndex.resolveCurrent(emptyList(), 0, days(60)))
    }

    @Test
    fun listShrinkingFromTheStartDoesNotLeaveIndexOutOfBounds() {
        // Looking at today in a 59 day diary, then all the videos disappear
        assertEquals(0, CalendarDayIndex.resolveCurrent(days(59), 58, days(1)))
    }

    @Test
    fun listGrowingAtTheStartStaysOnTheSameDate() {
        // Looking at today when the diary only had today, then older videos come back
        assertEquals(59, CalendarDayIndex.resolveCurrent(days(1), 0, days(60)))
    }

    @Test
    fun staysOnTheSameDateWhenOlderDaysAreRemoved() {
        val old = days(10)
        val newDays = days(5)
        // Index 7 in the old list is 2 days before today, which is index 2 in the new one
        assertEquals(2, CalendarDayIndex.resolveCurrent(old, 7, newDays))
    }

    @Test
    fun goesToLastDayWhenTheCurrentDateIsGone() {
        // Looking at a day older than anything left in the diary
        assertEquals(4, CalendarDayIndex.resolveCurrent(days(10), 0, days(5)))
    }

    @Test
    fun emptyNewListGivesZero() {
        assertEquals(0, CalendarDayIndex.resolveCurrent(days(3), 1, emptyList()))
    }
}
