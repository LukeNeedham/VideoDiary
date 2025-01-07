package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import kotlinx.coroutines.launch

class ExportDiaryViewViewModel(
    private val videoResolutionRepository: VideoResolutionRepository,
) : ViewModel() {
    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }
}