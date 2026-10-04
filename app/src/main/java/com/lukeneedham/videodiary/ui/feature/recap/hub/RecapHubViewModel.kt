package com.lukeneedham.videodiary.ui.feature.recap.hub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedRecapsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.domain.model.SavedRecap
import com.lukeneedham.videodiary.domain.util.date.CalendarUtil
import kotlinx.coroutines.launch
import java.io.File

data class SavedRecapWithThumbnails(
    val recap: SavedRecap,
    val dayVideoCount: Int,
    val thumbnailFiles: List<File>,
)

class RecapHubViewModel(
    private val savedRecapsDao: SavedRecapsDao,
    private val videosDao: VideosDao,
    private val videoResolutionRepository: VideoResolutionRepository,
) : ViewModel() {
    var savedRecaps: List<SavedRecapWithThumbnails> by mutableStateOf(emptyList())
        private set

    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
        viewModelScope.launch {
            savedRecapsDao.allSavedRecaps.collect { recaps ->
                savedRecaps = recaps.map { recap ->
                    val dates = CalendarUtil.getAllDates(recap.startDate, recap.endDate)
                    val recordedDates = dates.filter { videosDao.getVideoFileIfExists(it) != null }
                    val thumbnails = recordedDates.mapNotNull { videosDao.getThumbnailFileIfExists(it) }
                    SavedRecapWithThumbnails(
                        recap = recap,
                        dayVideoCount = recordedDates.size,
                        thumbnailFiles = thumbnails,
                    )
                }
            }
        }
    }

    fun deleteRecap(id: String) {
        viewModelScope.launch {
            savedRecapsDao.deleteSavedRecap(id)
        }
    }
}
