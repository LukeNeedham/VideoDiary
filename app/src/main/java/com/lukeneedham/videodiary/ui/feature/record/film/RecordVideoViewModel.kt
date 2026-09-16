package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecordVideoViewModel(
    private val date: LocalDate,
    private val settingsDao: SettingsDao,
    private val videoResolutionRepository: VideoResolutionRepository,
    private val videosDao: VideosDao,
) : ViewModel() {
    /** Whether [date] already has a video, before this recording. */
    val hasExistingVideo: Boolean = videosDao.getVideoFileIfExists(date) != null

    var resolution: Size? by mutableStateOf(null)
        private set

    var videoDurationMillis: Long? by mutableStateOf(null)
        private set

    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            resolution = settingsDao.getResolution()
        }

        viewModelScope.launch {
            videoDurationMillis = settingsDao.getVideoDuration()?.inWholeMilliseconds
        }

        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    /** Persists [videoContentUri] as the video for [date], skipping the review step. */
    fun persistVideoDirectly(videoContentUri: Uri) {
        videosDao.persistVideo(videoContentUri = videoContentUri, date = date)
    }
}