package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDay
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ExportDiaryCreateViewModel(
    private val videoExportDao: VideoExportDao,
    private val calendarRepository: CalendarRepository,
) : ViewModel() {
    private var allDays: List<Day> by mutableStateOf(emptyList())

    val totalVideoCount: Int? by derivedStateOf {
        allDays.count { it.video != null }
    }

    var exportState: ExportState by mutableStateOf(ExportState.Ready)
        private set

    var exportStartDate: LocalDate? by mutableStateOf(null)
    var exportEndDate: LocalDate? by mutableStateOf(null)

    var exportIncludeDateStamp: Boolean by mutableStateOf(true)

    private val selectedDays: List<ExportDay>? by derivedStateOf {
        val allDays = allDays
        if (allDays.isEmpty()) return@derivedStateOf null
        val startDate = exportStartDate ?: return@derivedStateOf null
        val endDate = exportEndDate ?: return@derivedStateOf null

        allDays.mapNotNull { day ->
            val date = day.date
            val isSelected = date in startDate..endDate
            if (!isSelected) return@mapNotNull null
            val video = day.video ?: return@mapNotNull null
            ExportDay(
                date = day.date,
                video = video,
            )
        }
    }

    val selectedVideoCount: Int? by derivedStateOf {
        selectedDays?.size
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
                if (exportStartDate == null) {
                    exportStartDate = days.firstOrNull()?.date
                }
                if (exportEndDate == null) {
                    exportEndDate = days.lastOrNull()?.date
                }
            }
        }
    }

    fun export() {
        val selectedDays = selectedDays
        if (selectedDays == null) {
            exportState = ExportState.Failed("Selected videos could not be computed")
            return
        }
        val endDate = exportEndDate
        if (endDate == null) {
            exportState = ExportState.Failed("End date is not set")
            return
        }
        val startDate = exportStartDate
        if (startDate == null) {
            exportState = ExportState.Failed("Start date is not set")
            return
        }

        viewModelScope.launch {
            videoExportDao.export(selectedDays, exportIncludeDateStamp).collect { state ->
                when (state) {
                    is VideoExportState.Failure -> {
                        val error = state.error
                        val errorMessage = error.message ?: error.toString()
                        exportState = ExportState.Failed(errorMessage)
                    }

                    is VideoExportState.InProgress -> {
                        exportState = ExportState.InProgress(state.progressFraction)
                    }

                    is VideoExportState.Success -> {
                        val exportedVideo = ExportedVideo(
                            videoFile = state.outputFile,
                            startDate = startDate,
                            endDate = endDate,
                            dayVideoCount = selectedDays.size,
                        )
                        exportState = ExportState.Ready
                        onExportedMutable.emit(exportedVideo)
                    }
                }
            }
        }
    }
}