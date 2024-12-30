package com.lukeneedham.videodiary.ui.feature.exportdiary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import kotlinx.coroutines.launch
import java.io.File

class ExportDiaryViewModel(
    private val videoExportDao: VideoExportDao,
    private val videoResolutionRepository: VideoResolutionRepository,
    private val calendarRepository: CalendarRepository,
) : ViewModel() {

    private var allVideos: List<File> = emptyList()

    var videoCount: Int? by mutableStateOf(null)
        private set

    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    var exportState: ExportState by mutableStateOf(ExportState.Ready)
        private set

    init {
        viewModelScope.launch {
            calendarRepository.allDays.collect { days ->
                val videos = days.mapNotNull { it.video }
                allVideos = videos
                videoCount = videos.size
            }
        }
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    fun export() {
        val videos = allVideos
        if (videos.isEmpty()) {
            exportState = ExportState.NoVideos
            return
        }
        exportState = ExportState.InProgress
        val outputFile = videoExportDao.export(videos)
        exportState = ExportState.Success(outputFile)
    }
}