package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedRecapsDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class RecapCreatedArgs(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val name: String,
    val savedRecapId: String,
)

class RecapCreateCustomViewModel(
    private val calendarRepository: CalendarRepository,
    private val savedRecapsDao: SavedRecapsDao,
) : ViewModel() {
    private var allDays: List<Day> by mutableStateOf(emptyList())

    val totalVideoCount: Int? by derivedStateOf {
        allDays.count { it.videoFile != null }
    }

    var recapStartDate: LocalDate? by mutableStateOf(null)
    var recapEndDate: LocalDate? by mutableStateOf(null)

    var recapName: String by mutableStateOf("")

    private val selectedDates: List<LocalDate>? by derivedStateOf {
        val allDays = allDays
        if (allDays.isEmpty()) return@derivedStateOf null
        val startDate = recapStartDate ?: return@derivedStateOf null
        val endDate = recapEndDate ?: return@derivedStateOf null

        allDays.mapNotNull { day ->
            val isSelected = day.date in startDate..endDate
            if (!isSelected || day.videoFile == null) return@mapNotNull null
            day.date
        }
    }

    val selectedVideoCount: Int? by derivedStateOf {
        selectedDates?.size
    }

    val selectedDayThumbnails: List<RecapDayThumbnail>? by derivedStateOf {
        val allDays = allDays
        if (allDays.isEmpty()) return@derivedStateOf null
        val startDate = recapStartDate ?: return@derivedStateOf null
        val endDate = recapEndDate ?: return@derivedStateOf null

        allDays.mapNotNull { day ->
            val isSelected = day.date in startDate..endDate
            if (!isSelected) return@mapNotNull null
            if (day.videoFile == null) return@mapNotNull null
            RecapDayThumbnail(
                date = day.date,
                thumbnailFile = day.thumbnailFile,
            )
        }
    }

    val diaryStartDate by derivedStateOf {
        allDays.firstOrNull()?.date
    }

    val canSave: Boolean by derivedStateOf {
        recapName.isNotBlank() && (selectedVideoCount ?: 0) > 0
    }

    private val onSavedMutable = MutableSharedFlow<RecapCreatedArgs>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onSavedFlow = onSavedMutable.asSharedFlow()

    init {
        viewModelScope.launch {
            calendarRepository.allDays.collect { days ->
                allDays = days
                if (recapStartDate == null) {
                    recapStartDate = days.firstOrNull()?.date
                }
                if (recapEndDate == null) {
                    recapEndDate = days.lastOrNull()?.date
                }
            }
        }
    }

    fun save() {
        if (!canSave) return
        val startDate = recapStartDate ?: return
        val endDate = recapEndDate ?: return
        val name = recapName.trim()

        viewModelScope.launch {
            val id = savedRecapsDao.saveRecap(name, startDate, endDate)
            onSavedMutable.emit(RecapCreatedArgs(startDate, endDate, name, id))
        }
    }
}
