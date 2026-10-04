package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import kotlinx.coroutines.launch

class RecapCreatePeriodListViewModel(
    val periodType: RecapPeriodType,
    private val calendarRepository: CalendarRepository,
) : ViewModel() {
    var options: List<RecapPeriodOption> by mutableStateOf(emptyList())
        private set

    var isLoaded: Boolean by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            calendarRepository.allDays.collect { days ->
                options = RecapPeriodOptions.build(days, periodType)
                isLoaded = true
            }
        }
    }
}
