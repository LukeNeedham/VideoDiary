package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import java.time.LocalDate

@Composable
fun ExportDiaryCreatePageContent(
    canGoBack: Boolean,
    onBack: () -> Unit,
    totalVideoCount: Int?,
    selectedVideoCount: Int?,
    diaryStartDate: LocalDate?,
    exportStartDate: LocalDate?,
    exportEndDate: LocalDate?,
    exportState: ExportState,
    onStartDateSelected: (LocalDate?) -> Unit,
    onEndDateSelected: (LocalDate?) -> Unit,
    export: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = canGoBack, onBack = onBack,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (exportStartDate == null || exportEndDate == null || totalVideoCount == null || diaryStartDate == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                ExportDiaryCreatePageReady(
                    totalVideoCount = totalVideoCount,
                    selectedVideoCount = selectedVideoCount,
                    diaryStartDate = diaryStartDate,
                    exportStartDate = exportStartDate,
                    exportEndDate = exportEndDate,
                    exportState = exportState,
                    onStartDateSelected = onStartDateSelected,
                    onEndDateSelected = onEndDateSelected,
                    export = export,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewExportDiaryPageContent() {
    Box(
        modifier = Modifier.background(Color.White)
    ) {
        ExportDiaryCreatePageContent(
            canGoBack = true,
            onBack = {},
            totalVideoCount = 10,
            selectedVideoCount = 5,
            exportState = MockDataExportDiaryCreate.exportState,
            export = {},
            exportStartDate = MockDataExportDiaryCreate.startDate,
            exportEndDate = MockDataExportDiaryCreate.endDate,
            diaryStartDate = MockDataExportDiaryCreate.diaryStartDate,
            onStartDateSelected = {},
            onEndDateSelected = {},
        )
    }
}