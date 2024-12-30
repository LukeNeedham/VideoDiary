package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.domain.model.Day
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val calendarRepository: CalendarRepository,
    private val videoResolutionRepository: VideoResolutionRepository,
    private val videosDao: VideosDao,
) : ViewModel() {
    var videoAspectRatio: Float? by mutableStateOf(null)

    var days by mutableStateOf<List<Day>>(emptyList())
        private set

    var startDate by mutableStateOf<LocalDate?>(null)
        private set

    var currentDayIndex by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            calendarRepository.allDays.collect { newDays ->
                val oldDays = days
                days = newDays
                if (oldDays.isEmpty()) {
                    // This is the first load
                    currentDayIndex = newDays.lastIndex
                }
            }
        }
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
        viewModelScope.launch {
            startDate = calendarRepository.getStartDate()
        }
    }

    fun deleteTodayVideo() {
        val today = LocalDate.now()
        videosDao.deleteVideo(today)
    }

    fun setCurrentDay(index: Int) {
        currentDayIndex = index
    }

    fun goToDate(date: LocalDate) {
        val index = days.indexOfFirst { day ->
            day.date == date
        }
        if (index != -1) {
            currentDayIndex = index
        }
    }
}