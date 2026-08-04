package com.lukeneedham.videodiary.ui.feature.crashlog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.CrashLogDao
import com.lukeneedham.videodiary.domain.model.CrashLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrashLogViewModel(
    private val crashLogDao: CrashLogDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    var crashLogs: List<CrashLog> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            crashLogs = withContext(ioDispatcher) {
                crashLogDao.getAllCrashLogs()
            }
        }
    }
}
