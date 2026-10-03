package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDay
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportOptions
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

class RecapShareViewModel(
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    private val name: String,
    private val videoExportDao: VideoExportDao,
) : ViewModel() {

    // Kept in sync with RecapViewViewModel.days by the page hosting this sheet - it's not known
    // yet when this ViewModel is first created, so it's pushed in rather than taken as a
    // constructor param.
    private var days: List<RecapDay> = emptyList()

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
    var state: RecapShareState by mutableStateOf(RecapShareState.SelectingOptions)
        private set

    private val onShareMutable = MutableSharedFlow<ShareRequest>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onShareFlow = onShareMutable.asSharedFlow()

    fun updateDays(newDays: List<RecapDay>) {
        days = newDays
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

        state = RecapShareState.InProgress(0f)
        viewModelScope.launch {
            videoExportDao.export(days, startDate, endDate, options).collect { exportState ->
                when (exportState) {
                    is VideoExportState.InProgress -> state = RecapShareState.InProgress(exportState.progressFraction)
                    is VideoExportState.Success -> markReady(exportState.outputFile)
                    is VideoExportState.Failure -> {
                        val error = exportState.error
                        state = RecapShareState.Failed(error.message ?: error.toString())
                    }

                    VideoExportState.Cancelled -> state = RecapShareState.SelectingOptions
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
        state = RecapShareState.SelectingOptions
    }

    /** Re-opens the system share sheet for the already-exported file - the same thing that
     * happens automatically the moment the export completes. */
    fun shareClicked() {
        val readyState = state as? RecapShareState.Ready ?: return
        emitShare(readyState.outputFile)
    }

    private fun markReady(file: File) {
        state = RecapShareState.Ready(file)
        emitShare(file)
    }

    private fun emitShare(file: File) {
        viewModelScope.launch {
            onShareMutable.emit(
                ShareRequest(
                    title = name,
                    text = name,
                    video = file,
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        videoExportDao.cancel()
    }
}
