package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportOptions
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

    fun onIncludeDateStampChange(value: Boolean) {
        includeDateStamp = value
    }

    fun startExport() {
        val existing = videoExportDao.getExistingExport(request.startDate, request.endDate, options)
        if (existing != null) {
            state = RecapShareState.Ready(existing)
            return
        }

        state = RecapShareState.InProgress(0f)
        viewModelScope.launch {
            videoExportDao.export(request.days, request.startDate, request.endDate, options).collect { exportState ->
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

    override fun onCleared() {
        super.onCleared()
        videoExportDao.cancel()
    }
}
