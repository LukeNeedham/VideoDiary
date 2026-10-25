package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val calendarRepository: CalendarRepository,
    private val videosDao: VideosDao,
    private val settingsDao: SettingsDao,
) : ViewModel() {

    /**
     * Video Player Controller, shared across the whole calendar.
     * This allows playback settings (like muted state) to persist across days.
     */
    val videoPlayerController = VideoPlayerController()

    var days by mutableStateOf<List<Day>>(emptyList())
        private set

    val startDate by derivedStateOf {
        days.firstOrNull()?.date ?: LocalDate.now()
    }

    var currentDayIndex by mutableIntStateOf(0)
        private set

    var allowEditPastDays by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            settingsDao.getAllowEditPastDaysFlow().collect { allow ->
                allowEditPastDays = allow
            }
        }
        viewModelScope.launch {
            calendarRepository.allDays.collect { newDays ->
                val oldDays = days
                val oldIndex = currentDayIndex
                days = newDays
                currentDayIndex = resolveCurrentDayIndex(oldDays, oldIndex, newDays)
            }
        }
    }

    fun deleteVideo(date: LocalDate) {
        videosDao.deleteVideo(date)
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

/**
 * The index to show once the days change to [newDays]. The list can grow or shrink at the start
 * when videos appear or disappear (for example when the storage location changes), so the index
 * alone is not enough: stay on the same date if it still exists, otherwise go to the last day.
 */
internal fun resolveCurrentDayIndex(
    oldDays: List<Day>,
    oldIndex: Int,
    newDays: List<Day>,
): Int {
    val currentDate = oldDays.getOrNull(oldIndex)?.date
    val newIndex = newDays.indexOfFirst { it.date == currentDate }
    return if (newIndex != -1) newIndex else newDays.lastIndex.coerceAtLeast(0)
}
