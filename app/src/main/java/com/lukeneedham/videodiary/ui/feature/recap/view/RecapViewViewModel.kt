package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedRecapsDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDay
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportRequest
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecapViewViewModel(
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    val name: String,
    initialSavedRecapId: String?,
    private val calendarRepository: CalendarRepository,
    private val savedRecapsDao: SavedRecapsDao,
    private val videoResolutionRepository: VideoResolutionRepository,
) : ViewModel() {
    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    var days: List<RecapDay> by mutableStateOf(emptyList())
        private set

    val videoFiles by derivedStateOf {
        days.map { it.video }
    }

    var savedRecapId: String? by mutableStateOf(initialSavedRecapId)
        private set

    val isSaved: Boolean by derivedStateOf {
        savedRecapId != null
    }

    var includeDateStamp: Boolean by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
        viewModelScope.launch {
            calendarRepository.allDays.collect { allDays ->
                days = allDays.mapNotNull { day ->
                    val videoFile = day.videoFile
                    if (day.date in startDate..endDate && videoFile != null) {
                        RecapDay(day.date, videoFile)
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun toggleSaved() {
        viewModelScope.launch {
            val id = savedRecapId
            if (id == null) {
                savedRecapId = savedRecapsDao.saveRecap(name, startDate, endDate)
            } else {
                savedRecapsDao.deleteSavedRecap(id)
                savedRecapId = null
            }
        }
    }

    fun onIncludeDateStampChange(value: Boolean) {
        includeDateStamp = value
    }

    fun buildExportRequest(): RecapExportRequest = RecapExportRequest(
        days = days,
        startDate = startDate,
        endDate = endDate,
        includeDateStamp = includeDateStamp,
        name = name,
    )
}
