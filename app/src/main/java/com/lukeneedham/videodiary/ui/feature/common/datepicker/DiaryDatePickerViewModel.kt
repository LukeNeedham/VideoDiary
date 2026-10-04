package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.domain.model.Day
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.YearMonth

class DiaryDatePickerViewModel(
    private val calendarRepository: CalendarRepository,
    private val videoResolutionRepository: VideoResolutionRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    private val scope = CoroutineScope(mainDispatcher)

    /** All days of the diary, grouped by calendar month in chronological order */
    var months by mutableStateOf<List<List<Day>>>(emptyList())
        private set

    var videoAspectRatio by mutableStateOf<Float?>(null)
        private set

    init {
        scope.launch {
            calendarRepository.allDays.collect { allDays ->
                months = allDays
                    .groupBy { YearMonth.from(it.date) }
                    .toSortedMap()
                    .values
                    .toList()
            }
        }
        scope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    fun dispose() {
        scope.cancel()
    }
}