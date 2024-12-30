package com.lukeneedham.videodiary.data.repository

import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.Day
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class CalendarRepository(
    private val videosDao: VideosDao,
    private val settingsDao: SettingsDao,
) {
    private var allDatesCached: List<LocalDate>? = null

    val allDays = videosDao.allVideos.map {
        getAllDays()
    }

    suspend fun getStartDate() = settingsDao.getStartDate()

    private suspend fun getAllDays(): List<Day> {
        return getAllDates().map { date ->
            val file = videosDao.getVideoFile(date)
            val existingFile = if (file.exists()) file else null
            Day(date, existingFile)
        }
    }

    /** @return an ordered list of all dates between the start date and today (both inclusive) */
    private suspend fun getAllDates(): List<LocalDate> {
        val cached = allDatesCached
        if (cached != null) return cached

        val today = LocalDate.now()
        val startDate = getStartDate() ?: return emptyList()
        var date = startDate
        val allDates = mutableListOf<LocalDate>()
        while (date <= today) {
            allDates.add(date)
            date = date.plusDays(1)
        }
        allDatesCached = allDates
        return allDates
    }
}