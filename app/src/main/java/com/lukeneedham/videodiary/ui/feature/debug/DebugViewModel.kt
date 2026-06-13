package com.lukeneedham.videodiary.ui.feature.debug

import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.repository.MockDataRepository

class DebugViewModel(
    private val mockDataRepository: MockDataRepository,
) : ViewModel() {
    fun fillWithMockData() {
        mockDataRepository.fillWithMockData()
    }
}
