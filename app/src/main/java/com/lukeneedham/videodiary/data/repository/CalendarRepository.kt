package com.lukeneedham.videodiary.data.repository

import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.Day
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class CalendarRepository(
    private val videosDao: VideosDao,
    private val videoFileNameMapper: VideoFileNameMapper,
) {
    val allDays = videosDao.allVideos.map { allVideos ->
        val today = LocalDate.now()
        val startDate =
            allVideos.minOfOrNull { videoFileNameMapper.nameToDate(it.name) } ?: today
        getAllDays(startDate)
    }

    private fun getAllDays(startDate: LocalDate): List<Day> {
        return getAllDates(startDate).map { date ->
            val file = videosDao.getVideoFile(date)
            val existingFile = if (file.exists()) file else null
            Day(date, existingFile)
        }
    }

    /** @return an ordered list of all dates between the start date and today (both inclusive) */
    private fun getAllDates(startDate: LocalDate): List<LocalDate> {
        val today = LocalDate.now()
        var date = startDate
        val allDates = mutableListOf<LocalDate>()
        while (date <= today) {
            allDates.add(date)
            date = date.plusDays(1)
        }
        return allDates
    }
}