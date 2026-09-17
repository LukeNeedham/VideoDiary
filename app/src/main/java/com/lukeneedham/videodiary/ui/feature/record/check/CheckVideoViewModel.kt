package com.lukeneedham.videodiary.ui.feature.record.check

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
    val newVideo = Video.MediaStore(videoContentUri)

    /** The video already recorded for [date], before this new one. Only reached when one exists. */
    val existingVideo = Video.PersistedFile(
        requireNotNull(videosDao.getVideoFileIfExists(date)) {
            "CheckVideoPage requires an existing video for $date"
        }
    )

    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    /** Keeps the newly recorded video, overwriting the existing video for [date]. */
    fun keepNewVideo() {
        videosDao.persistVideo(
            videoContentUri = videoContentUri,
            date = date,
        )
    }

    /** Discards the newly recorded video, leaving the existing video for [date] untouched. */
    fun discardNewVideo() {
        videosDao.discardMediaStoreVideo(videoContentUri)
    }
}
