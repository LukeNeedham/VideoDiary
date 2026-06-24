package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedExportsDao
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDay
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDayThumbnail
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ExportDiaryCreateViewModel(
    private val videoExportDao: VideoExportDao,
    private val calendarRepository: CalendarRepository,
    private val savedExportsDao: SavedExportsDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var allDays: List<Day> by mutableStateOf(emptyList())

    val totalVideoCount: Int? by derivedStateOf {
        allDays.count { it.videoFile != null }
    }

    var exportState: ExportState by mutableStateOf(ExportState.Ready)
        private set

    var exportStartDate: LocalDate? by mutableStateOf(null)
    var exportEndDate: LocalDate? by mutableStateOf(null)

    var exportIncludeDateStamp: Boolean by mutableStateOf(true)

    var exportName: String by mutableStateOf("")

    private val selectedDays: List<ExportDay>? by derivedStateOf {
        val allDays = allDays
        if (allDays.isEmpty()) return@derivedStateOf null
        val startDate = exportStartDate ?: return@derivedStateOf null
        val endDate = exportEndDate ?: return@derivedStateOf null

        allDays.mapNotNull { day ->
            val date = day.date
            val isSelected = date in startDate..endDate
            if (!isSelected) return@mapNotNull null
            val video = day.videoFile ?: return@mapNotNull null
            ExportDay(
                date = day.date,
                video = video,
            )
        }
    }

    val selectedVideoCount: Int? by derivedStateOf {
        selectedDays?.size
    }

    val selectedDayThumbnails: List<ExportDayThumbnail>? by derivedStateOf {
        val allDays = allDays
        if (allDays.isEmpty()) return@derivedStateOf null
        val startDate = exportStartDate ?: return@derivedStateOf null
        val endDate = exportEndDate ?: return@derivedStateOf null

        allDays.mapNotNull { day ->
            val isSelected = day.date in startDate..endDate
            if (!isSelected) return@mapNotNull null
            if (day.videoFile == null) return@mapNotNull null
            ExportDayThumbnail(
                date = day.date,
                thumbnailFile = day.thumbnailFile,
            )
        }
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

                        val trimmedName = exportName.trim()
                        if (trimmedName.isNotEmpty()) {
                            withContext(ioDispatcher) {
                                savedExportsDao.saveExport(trimmedName, exportedVideo)
                            }
                        }

                        exportState = ExportState.Ready
                        onExportedMutable.emit(exportedVideo)
                    }
                }
            }
        }
    }
}
