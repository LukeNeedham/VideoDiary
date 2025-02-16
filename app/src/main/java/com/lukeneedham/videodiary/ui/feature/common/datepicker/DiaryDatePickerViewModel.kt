package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.util.date.CalendarUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DiaryDatePickerViewModel(
    private val calendarRepository: CalendarRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    private val scope = CoroutineScope(mainDispatcher)

    var weeks by mutableStateOf<List<List<Day>>>(emptyList())
        private set

    init {
        scope.launch {
            calendarRepository.allDays.collect { allDays ->
                weeks = CalendarUtil.chunkIntoWeeks(
                    items = allDays,
                    getDate = { it.date }
                )
            }
        }
    }

    fun dispose() {
        scope.cancel()
    }
}