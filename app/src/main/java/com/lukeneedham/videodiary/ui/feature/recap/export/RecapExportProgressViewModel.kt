package com.lukeneedham.videodiary.ui.feature.recap.export

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.export.model.RecapExportProgressState
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RecapExportProgressViewModel(
    private val request: RecapExportRequest,
    private val videoExportDao: VideoExportDao,
) : ViewModel() {

    var progressState: RecapExportProgressState by mutableStateOf(RecapExportProgressState.InProgress(0f))
        private set

    var isCancelling: Boolean by mutableStateOf(false)
        private set

    private val onShareMutable = MutableSharedFlow<ShareRequest>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onShareFlow = onShareMutable.asSharedFlow()

    private val onExitMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onExitFlow = onExitMutable.asSharedFlow()

    init {
        viewModelScope.launch {
            videoExportDao.export(request.days, request.includeDateStamp).collect { state ->
                when (state) {
                    is VideoExportState.Failure -> {
                        val error = state.error
                        progressState = RecapExportProgressState.Failed(error.message ?: error.toString())
                    }

                    is VideoExportState.InProgress -> {
                        progressState = RecapExportProgressState.InProgress(state.progressFraction)
                    }

                    is VideoExportState.Success -> {
                        val shareRequest = ShareRequest(
                            title = request.name,
                            text = request.name,
                            video = state.outputFile,
                        )
                        onShareMutable.emit(shareRequest)
                    }

                    VideoExportState.Cancelled -> {
                        onExitMutable.emit(Unit)
                    }
                }
            }
        }
    }

    fun cancel() {
        if (isCancelling) return
        isCancelling = true
        videoExportDao.cancel()
    }

    fun dismissFailure() {
        viewModelScope.launch {
            onExitMutable.emit(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()
        videoExportDao.cancel()
    }
}
