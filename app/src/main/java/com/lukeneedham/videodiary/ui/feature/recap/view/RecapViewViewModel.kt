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
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportRequest
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

class RecapViewViewModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val name: String,
    initialSavedRecapId: String?,
    private val calendarRepository: CalendarRepository,
    private val savedRecapsDao: SavedRecapsDao,
    private val videoResolutionRepository: VideoResolutionRepository,
) : ViewModel() {
    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    private var days: List<RecapDay> by mutableStateOf(emptyList())

    var dayThumbnails: List<RecapDayThumbnail> by mutableStateOf(emptyList())
        private set

    val videoFiles: List<File> by derivedStateOf {
        days.map { it.video }
    }

    var savedRecapId: String? by mutableStateOf(initialSavedRecapId)
        private set

    val isSaved: Boolean by derivedStateOf {
        savedRecapId != null
    }

    var includeDateStamp: Boolean by mutableStateOf(true)

    val exportRequest: RecapExportRequest? by derivedStateOf {
        val days = days
        if (days.isEmpty()) return@derivedStateOf null
        RecapExportRequest(
            days = days,
            startDate = startDate,
            endDate = endDate,
            includeDateStamp = includeDateStamp,
            name = name,
        )
    }

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
        viewModelScope.launch {
            calendarRepository.allDays.collect { allDays ->
                val inRange = allDays.filter { it.date in startDate..endDate && it.videoFile != null }
                days = inRange.map { day -> RecapDay(date = day.date, video = day.videoFile!!) }
                dayThumbnails = inRange.map { day ->
                    RecapDayThumbnail(date = day.date, thumbnailFile = day.thumbnailFile)
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
}
