package com.lukeneedham.videodiary.ui.feature.exportdiary.progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedExportsDao
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.export.VideoExportState
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportRequest
import com.lukeneedham.videodiary.ui.feature.exportdiary.progress.model.ExportProgressState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExportDiaryProgressViewModel(
    private val exportRequest: ExportRequest,
    private val videoExportDao: VideoExportDao,
    private val savedExportsDao: SavedExportsDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var progressState: ExportProgressState by mutableStateOf(ExportProgressState.InProgress(0f))
        private set

    var isCancelling: Boolean by mutableStateOf(false)
        private set

    private val onExportedMutable = MutableSharedFlow<ExportedVideo>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onExportedFlow = onExportedMutable.asSharedFlow()

    private val onExitMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onExitFlow = onExitMutable.asSharedFlow()

    init {
        viewModelScope.launch {
            videoExportDao.export(exportRequest.days, exportRequest.includeDateStamp).collect { state ->
                when (state) {
                    is VideoExportState.Failure -> {
                        val error = state.error
                        progressState = ExportProgressState.Failed(error.message ?: error.toString())
                    }

                    is VideoExportState.InProgress -> {
                        progressState = ExportProgressState.InProgress(state.progressFraction)
                    }

                    is VideoExportState.Success -> {
                        val name = exportRequest.name
                        val exportedVideo = ExportedVideo(
                            videoFile = state.outputFile,
                            name = name.ifEmpty { null },
                            startDate = exportRequest.startDate,
                            endDate = exportRequest.endDate,
                            dayVideoCount = exportRequest.days.size,
                        )

                        if (name.isNotEmpty()) {
                            withContext(ioDispatcher) {
                                savedExportsDao.saveExport(
                                    name,
                                    exportedVideo,
                                    exportRequest.days.map { it.date },
                                )
                            }
                        }

                        onExportedMutable.emit(exportedVideo)
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
