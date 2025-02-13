package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
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
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.datepicker.DiaryDatePickerDialog
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.component.ExportDiaryCreateDatePicker
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.component.ExportDiaryEmptyCreate
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportState
import java.time.LocalDate

@Composable
fun ExportDiaryCreatePageReady(
    totalVideoCount: Int,
    selectedVideoCount: Int?,
    diaryStartDate: LocalDate,
    exportStartDate: LocalDate,
    exportEndDate: LocalDate,
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
                        date = exportStartDate,
                        label = "From",
                        onChange = {
                            showStartDatePicker = true
                        },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ExportDiaryCreateDatePicker(
                        date = exportEndDate,
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
                        is ExportState.InProgress -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Export in progress..."
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                LinearProgressIndicator(
                                    progress = { exportState.progressFraction },
                                )
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
        if (showStartDatePicker) {
            DiaryDatePickerDialog(
                firstPossibleDate = diaryStartDate,
                selectedDate = exportStartDate,
                onDateSelected = onStartDateSelected,
                onDismiss = {
                    showStartDatePicker = false
                },
            )
        }
        if (showEndDatePicker) {
            DiaryDatePickerDialog(
                firstPossibleDate = exportStartDate,
                selectedDate = exportEndDate,
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
internal fun PreviewExportDiaryCreatePageReady() {
    ExportDiaryCreatePageReady(
        totalVideoCount = 10,
        selectedVideoCount = 5,
        diaryStartDate = MockDataExportDiaryCreate.diaryStartDate,
        exportStartDate = MockDataExportDiaryCreate.startDate,
        exportEndDate = MockDataExportDiaryCreate.endDate,
        exportState = MockDataExportDiaryCreate.exportState,
        onStartDateSelected = {},
        onEndDateSelected = {},
        export = {},
    )
}