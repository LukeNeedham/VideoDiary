package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedRecapsDao
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDay
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportOptions
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import java.io.File
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecapViewViewModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    initialName: String,
    initialSavedRecapId: String?,
    private val calendarRepository: CalendarRepository,
    private val savedRecapsDao: SavedRecapsDao,
    private val videoExportDao: VideoExportDao,
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

    // Each export option gets its own backing property here (bound to its own control on the
    // options screen), folded together into `options` below - the single thing actually passed
    // to the exporter/cache, so adding a new option only means adding it in these two places.
    var includeDateStamp: Boolean by mutableStateOf(false)
        private set

    private val options: RecapExportOptions
        get() = RecapExportOptions(includeDateStamp = includeDateStamp)

    // Always starts on the options screen, even if a previous export happens to already match
    // the default options - the user picks options first, and only then (in startExport) do we
    // check whether that exact combination is already cached.
    var shareState: RecapShareState by mutableStateOf(RecapShareState.SelectingOptions)
        private set

    private val onShareMutable = MutableSharedFlow<ShareRequest>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onShareFlow = onShareMutable.asSharedFlow()

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

    fun onIncludeDateStampChange(value: Boolean) {
        includeDateStamp = value
    }

    fun startExport() {
        val existing = videoExportDao.getExistingExport(startDate, endDate, options)
        if (existing != null) {
            markReady(existing)
            return
        }

        shareState = RecapShareState.InProgress(0f)
        viewModelScope.launch {
            videoExportDao.export(days, startDate, endDate, options).collect { exportState ->
                when (exportState) {
                    is VideoExportState.InProgress -> shareState = RecapShareState.InProgress(exportState.progressFraction)
                    is VideoExportState.Success -> markReady(exportState.outputFile)
                    is VideoExportState.Failure -> {
                        val error = exportState.error
                        shareState = RecapShareState.Failed(error.message ?: error.toString())
                    }

                    VideoExportState.Cancelled -> shareState = RecapShareState.SelectingOptions
                }
            }
        }
    }

    fun cancelExport() {
        videoExportDao.cancel()
    }

    /** Back to the options screen - used both to retry after a failure and, from the ready
     * screen, to pick different options and potentially make a new export. */
    fun backToOptions() {
        shareState = RecapShareState.SelectingOptions
    }

    /** Opens the system share sheet for the already-exported file. */
    fun shareClicked() {
        val readyState = shareState as? RecapShareState.Ready ?: return
        viewModelScope.launch {
            onShareMutable.emit(
                ShareRequest(
                    title = name,
                    text = name,
                    video = readyState.outputFile,
                )
            )
        }
    }

    private fun markReady(file: File) {
        shareState = RecapShareState.Ready(file)
    }

    override fun onCleared() {
        super.onCleared()
        videoExportDao.cancel()
    }
}
