package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RecapShareViewModel(
    private val request: RecapShareRequest,
    private val videoExportDao: VideoExportDao,
) : ViewModel() {

    var includeDateStamp: Boolean by mutableStateOf(false)
        private set

    var state: RecapShareState by mutableStateOf(stateForCurrentOptions())
        private set

    private val onShareMutable = MutableSharedFlow<ShareRequest>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onShareFlow = onShareMutable.asSharedFlow()

    fun onIncludeDateStampChange(value: Boolean) {
        includeDateStamp = value
        // Switching options might point at a combination that's already been exported before.
        state = stateForCurrentOptions()
    }

    fun startExport() {
        val existing = videoExportDao.getExistingExport(request.startDate, request.endDate, includeDateStamp)
        if (existing != null) {
            state = RecapShareState.Ready(existing)
            return
        }

        state = RecapShareState.InProgress(0f)
        viewModelScope.launch {
            videoExportDao.export(request.days, request.startDate, request.endDate, includeDateStamp).collect { exportState ->
                state = when (exportState) {
                    is VideoExportState.InProgress -> RecapShareState.InProgress(exportState.progressFraction)
                    is VideoExportState.Success -> RecapShareState.Ready(exportState.outputFile)
                    is VideoExportState.Failure -> {
                        val error = exportState.error
                        RecapShareState.Failed(error.message ?: error.toString())
                    }

                    VideoExportState.Cancelled -> RecapShareState.SelectingOptions
                }
            }
        }
    }

    fun cancelExport() {
        videoExportDao.cancel()
    }

    fun retryAfterFailure() {
        state = RecapShareState.SelectingOptions
    }

    fun shareClicked() {
        val readyState = state as? RecapShareState.Ready ?: return
        viewModelScope.launch {
            onShareMutable.emit(
                ShareRequest(
                    title = request.name,
                    text = request.name,
                    video = readyState.outputFile,
                )
            )
        }
    }

    /** A previously-exported file for the current options, ready to share immediately - or, if
     * none exists yet, the options screen so the user can create one. */
    private fun stateForCurrentOptions(): RecapShareState {
        val existing = videoExportDao.getExistingExport(request.startDate, request.endDate, includeDateStamp)
        return if (existing != null) RecapShareState.Ready(existing) else RecapShareState.SelectingOptions
    }

    override fun onCleared() {
        super.onCleared()
        videoExportDao.cancel()
    }
}
