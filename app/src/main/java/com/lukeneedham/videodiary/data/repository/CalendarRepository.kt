package com.lukeneedham.videodiary.data.repository

import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.util.date.CalendarUtil
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

class CalendarRepository(
    private val videosDao: VideosDao,
    private val videoFileNameMapper: VideoFileNameMapper,
    private val currentDateRepository: CurrentDateRepository,
) {
    val allDays = combine(
        videosDao.allVideos,
        currentDateRepository.currentDate,
    ) { allVideos, today ->
        val startDate =
            allVideos.minOfOrNull { videoFileNameMapper.nameToDate(it.name) } ?: today
        getAllDays(startDate, today)
    }

    private fun getAllDays(startDate: LocalDate, today: LocalDate): List<Day> {
        return getAllDates(startDate, today).map { date ->
            val videoFile = videosDao.getVideoFileIfExists(date)
            val thumbnailFile = videosDao.getThumbnailFileIfExists(date)
            Day(date = date, videoFile = videoFile, thumbnailFile = thumbnailFile)
        }
    }

    /** @return an ordered list of all dates between the start date and today (both inclusive) */
    private fun getAllDates(startDate: LocalDate, today: LocalDate) =
        CalendarUtil.getAllDates(startDate, today)
}