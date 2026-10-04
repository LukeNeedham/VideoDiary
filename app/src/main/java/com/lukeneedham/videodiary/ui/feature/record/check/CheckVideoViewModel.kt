package com.lukeneedham.videodiary.ui.feature.record.check

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.Video
import java.time.LocalDate

class CheckVideoViewModel(
    val date: LocalDate,
    val videoContentUri: Uri,
    private val videosDao: VideosDao,
) : ViewModel() {
    val newVideo = Video.MediaStore(videoContentUri)

    /** The video already recorded for [date], before this new one. Only reached when one exists. */
    val existingVideo = Video.PersistedFile(
        requireNotNull(videosDao.getVideoFileIfExists(date)) {
            "CheckVideoPage requires an existing video for $date"
        }
    )

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
