package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedRecapsDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDay
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecapViewViewModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    initialName: String,
    initialSavedRecapId: String?,
    private val calendarRepository: CalendarRepository,
    private val savedRecapsDao: SavedRecapsDao,
) : ViewModel() {
    var name: String by mutableStateOf(initialName)
        private set

    var days: List<RecapDay> by mutableStateOf(emptyList())
        private set

    var thumbnails: List<RecapDayThumbnail> by mutableStateOf(emptyList())
        private set

    val videoFiles by derivedStateOf {
        days.map { it.video }
    }

    var savedRecapId: String? by mutableStateOf(initialSavedRecapId)
        private set

    val isSaved: Boolean by derivedStateOf {
        savedRecapId != null
    }

    init {
        viewModelScope.launch {
            calendarRepository.allDays.collect { allDays ->
                thumbnails = allDays.mapNotNull { day ->
                    if (day.date in startDate..endDate && day.videoFile != null) {
                        RecapDayThumbnail(day.date, day.thumbnailFile)
                    } else {
                        null
                    }
                }
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

    /** Renames the recap, including its saved copy if it has been saved. */
    fun rename(newName: String) {
        val trimmed = newName.trim()
        if (trimmed.isEmpty()) return
        name = trimmed
        val id = savedRecapId ?: return
        viewModelScope.launch {
            savedRecapsDao.renameSavedRecap(id, trimmed)
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
