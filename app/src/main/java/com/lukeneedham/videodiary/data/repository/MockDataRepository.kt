package com.lukeneedham.videodiary.data.repository

import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.util.date.CalendarUtil
import java.time.LocalDate
import kotlin.random.Random

class MockDataRepository(
    private val videosDao: VideosDao,
) {
    /**
     * Overwrites all existing diary data with a random mix of mock videos,
     * spanning the last [weeksOfData] weeks.
     */
    fun fillWithMockData() {
        val today = LocalDate.now()
        val startDate = today.minusWeeks(weeksOfData).plusDays(1)
        val allDates = CalendarUtil.getAllDates(startDate, today)

        val datesWithVideo = allDates.filterIndexed { index, _ ->
            // Always include the oldest date, so the calendar spans the full range
            index == 0 || Random.nextBoolean()
        }

        val realStartDate = startDate.minusDays(17)
        val allDatesWithVideo = listOf(realStartDate) + datesWithVideo

        videosDao.fillWithMockVideos(allDatesWithVideo)
    }

    companion object {
        private const val weeksOfData = 5L
    }
}
