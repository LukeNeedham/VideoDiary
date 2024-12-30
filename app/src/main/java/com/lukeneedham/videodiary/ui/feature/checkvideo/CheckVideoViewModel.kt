package com.lukeneedham.videodiary.ui.feature.checkvideo

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.domain.model.Video
import kotlinx.coroutines.launch
import java.time.LocalDate

class CheckVideoViewModel(
    val date: LocalDate,
    val videoContentUri: Uri,
    private val videosDao: VideosDao,
    private val videoResolutionRepository: VideoResolutionRepository,
) : ViewModel() {
    val video = Video.MediaStore(videoContentUri)

    var videoAspectRatio: Float? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    fun acceptVideo() {
        videosDao.persistVideo(
            videoContentUri = videoContentUri,
            date = date,
        )
    }
}