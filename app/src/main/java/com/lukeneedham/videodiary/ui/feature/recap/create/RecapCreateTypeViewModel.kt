package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import kotlinx.coroutines.launch

class RecapCreateTypeViewModel(
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
