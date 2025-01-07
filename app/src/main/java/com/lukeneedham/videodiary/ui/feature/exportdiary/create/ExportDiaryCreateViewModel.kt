package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

class ExportDiaryCreateViewModel(
    private val videoExportDao: VideoExportDao,
    private val calendarRepository: CalendarRepository,
) : ViewModel() {
    private val today = LocalDate.now()

    private var allDays: List<Day> by mutableStateOf(emptyList())

    val totalVideoCount: Int? by derivedStateOf {
        allDays.count { it.video != null }
    }

    var exportState: ExportState by mutableStateOf(ExportState.Ready)
        private set

    var exportStartDate: LocalDate by mutableStateOf(today)

    var exportEndDate: LocalDate? by mutableStateOf(null)

    val exportEndDateOrDefault by derivedStateOf {
        exportEndDate ?: today
    }

    private val selectedVideos: List<File>? by derivedStateOf {
        val allDays = allDays
        if (allDays.isEmpty()) return@derivedStateOf null
        val startDate = exportStartDate
        val endDate = exportEndDate ?: return@derivedStateOf null

        val selectedDays = allDays.filter {
            val date = it.date
            date in startDate..endDate
        }
        selectedDays.mapNotNull { it.video }
    }

    val selectedVideoCount: Int? by derivedStateOf {
        selectedVideos?.size
    }

    val diaryStartDate by derivedStateOf {
        allDays.firstOrNull()?.date
    }

    private val onExportedMutable = MutableSharedFlow<ExportedVideo>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onExportedFlow = onExportedMutable.asSharedFlow()

    init {
        viewModelScope.launch {
            calendarRepository.allDays.collect { days ->
                allDays = days
                // Update end date to the date of the last video
                // only when the user hasn't already selected another explicit end date
                if (exportEndDate == null) {
                    exportEndDate = days.lastOrNull()?.date
                }
            }
        }
    }

    fun export() {
        val selectedVideos = selectedVideos
        if (selectedVideos == null) {
            exportState = ExportState.Failed("Selected videos could not be computed")
            return
        }
        val endDate = exportEndDate
        if (endDate == null) {
            exportState = ExportState.Failed("End date is not set")
            return
        }

        exportState = ExportState.InProgress
        val outputFile = videoExportDao.export(selectedVideos)
        val exportedVideo = ExportedVideo(
            videoFile = outputFile,
            startDate = exportStartDate,
            endDate = endDate,
            dayVideoCount = selectedVideos.size,
        )
        viewModelScope.launch {
            exportState = ExportState.Ready
            onExportedMutable.emit(exportedVideo)
        }
    }
}