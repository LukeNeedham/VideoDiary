package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.datepicker.DiaryDatePickerDialog
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.component.ExportDiaryCreateDatePicker
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.component.ExportDiaryEmptyCreate
import java.time.LocalDate

@Composable
fun ExportDiaryCreatePageContent(
    canGoBack: Boolean,
    onBack: () -> Unit,
    totalVideoCount: Int?,
    selectedVideoCount: Int?,
    diaryStartDate: LocalDate?,
    startDate: LocalDate,
    endDate: LocalDate,
    exportState: ExportState,
    onStartDateSelected: (LocalDate?) -> Unit,
    onEndDateSelected: (LocalDate?) -> Unit,
    export: () -> Unit,
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            GenericToolbar(
                canGoBack = canGoBack, onBack = onBack,
            )

            if (totalVideoCount != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp)
                ) {
                    if (totalVideoCount == 0) {
                        ExportDiaryEmptyCreate()
                    } else {
                        Text(
                            text = "Export will include videos..."
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        ExportDiaryCreateDatePicker(
                            date = startDate,
                            label = "From",
                            onChange = {
                                showStartDatePicker = true
                            },
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ExportDiaryCreateDatePicker(
                            date = endDate,
                            label = "To",
                            onChange = {
                                showEndDatePicker = true
                            },
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = "Export will include $selectedVideoCount of your $totalVideoCount diary videos"
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        when (exportState) {
                            ExportState.InProgress -> {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is ExportState.Failed -> {
                                Text(
                                    text = "Something went wrong during the export: ${exportState.error}"
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Feel free to try again"
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Button(
                                    text = "Export",
                                    onClick = export,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            ExportState.Ready -> {
                                if (selectedVideoCount == 0) {
                                    Text(
                                        text = "Cannot export - please select at least one video",
                                    )
                                } else {
                                    Button(
                                        text = "Export",
                                        onClick = export,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showStartDatePicker) {
            if (diaryStartDate != null) {
                DiaryDatePickerDialog(
                    firstPossibleDate = diaryStartDate,
                    selectedDate = startDate,
                    onDateSelected = onStartDateSelected,
                    onDismiss = {
                        showStartDatePicker = false
                    },
                )
            }
        }
        if (showEndDatePicker) {
            DiaryDatePickerDialog(
                firstPossibleDate = startDate,
                selectedDate = endDate,
                onDateSelected = onEndDateSelected,
                onDismiss = {
                    showEndDatePicker = false
                },
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewExportDiaryPageContent() {
    ExportDiaryCreatePageContent(
        canGoBack = true,
        onBack = {},
        totalVideoCount = 10,
        selectedVideoCount = 5,
        exportState = MockDataExportDiaryCreate.exportState,
        export = {},
        startDate = MockDataExportDiaryCreate.startDate,
        endDate = MockDataExportDiaryCreate.endDate,
        diaryStartDate = MockDataExportDiaryCreate.diaryStartDate,
        onStartDateSelected = {},
        onEndDateSelected = {},
    )
}