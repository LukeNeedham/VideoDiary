package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDay
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDayThumbnail
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportRequest
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExportDiaryCreateViewModel(
    private val calendarRepository: CalendarRepository,
) : ViewModel() {
    private var allDays: List<Day> by mutableStateOf(emptyList())

    val totalVideoCount: Int? by derivedStateOf {
        allDays.count { it.videoFile != null }
    }

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

    val exportRequest: ExportRequest? by derivedStateOf {
        val days = selectedDays ?: return@derivedStateOf null
        val startDate = exportStartDate ?: return@derivedStateOf null
        val endDate = exportEndDate ?: return@derivedStateOf null
        ExportRequest(
            days = days,
            startDate = startDate,
            endDate = endDate,
            includeDateStamp = exportIncludeDateStamp,
            name = exportName.trim(),
        )
    }

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
}
