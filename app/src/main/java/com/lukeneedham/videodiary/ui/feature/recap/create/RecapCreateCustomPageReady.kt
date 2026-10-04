package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.datepicker.DiaryDatePickerDialog
import com.lukeneedham.videodiary.ui.feature.recap.create.component.RecapCreateEmpty
import com.lukeneedham.videodiary.ui.feature.recap.create.component.RecapDatePicker
import com.lukeneedham.videodiary.ui.feature.recap.create.component.RecapThumbnailRow
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AppSurface
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.LocalDate

@Composable
fun RecapCreateCustomPageReady(
    totalVideoCount: Int,
    selectedVideoCount: Int?,
    selectedDayThumbnails: List<RecapDayThumbnail>?,
    recapStartDate: LocalDate,
    recapEndDate: LocalDate,
    onStartDateSelected: (LocalDate?) -> Unit,
    onEndDateSelected: (LocalDate?) -> Unit,
    recapName: String,
    onRecapNameChange: (String) -> Unit,
    canSave: Boolean,
    onSaveClick: () -> Unit,
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (totalVideoCount == 0) {
            RecapCreateEmpty()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        Text(
                            text = "Custom recap",
                            color = Color.White,
                            fontSize = Typography.Size.big,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Choose the dates to include and name your recap",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = Typography.Size.extraSmall,
                        )
                    }

                    RecapSection(title = "Date range") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            RecapDatePicker(
                                label = "From",
                                date = recapStartDate,
                                onClick = { showStartDatePicker = true },
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = "→",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = Typography.Size.big,
                            )
                            RecapDatePicker(
                                label = "To",
                                date = recapEndDate,
                                onClick = { showEndDatePicker = true },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    RecapSection(title = "Name") {
                        TextField(
                            value = recapName,
                            onValueChange = onRecapNameChange,
                            placeholder = {
                                Text(
                                    text = "e.g. Summer 2024",
                                    color = Color.White.copy(alpha = 0.4f),
                                )
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.White,
                                backgroundColor = AppSurfaceVariant,
                                cursorColor = AccentHighlight,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    if (!selectedDayThumbnails.isNullOrEmpty()) {
                        RecapSection(
                            title = if (selectedVideoCount != null) {
                                "Videos to include ($selectedVideoCount of $totalVideoCount)"
                            } else {
                                "Videos to include"
                            }
                        ) {
                            RecapThumbnailRow(thumbnails = selectedDayThumbnails)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (selectedVideoCount == 0) {
                    Text(
                        text = "Cannot create recap - please select at least one video",
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = Typography.Size.extraSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        text = "Save recap",
                        onClick = onSaveClick,
                        enabled = canSave,
                        backgroundColor = AccentHighlight,
                        foregroundColor = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        if (showStartDatePicker) {
            DiaryDatePickerDialog(
                onDateSelected = onStartDateSelected,
                initialFocusedDate = recapStartDate,
                onDismiss = { showStartDatePicker = false },
            )
        }
        if (showEndDatePicker) {
            DiaryDatePickerDialog(
                initialFocusedDate = recapEndDate,
                onDateSelected = onEndDateSelected,
                onDismiss = { showEndDatePicker = false },
            )
        }
    }
}

@Composable
private fun RecapSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppSurface)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = Typography.Size.medium,
            fontWeight = FontWeight.SemiBold,
        )
        content()
    }
}

@Preview
@Composable
internal fun PreviewRecapCreateCustomPageReady() {
    RecapCreateCustomPageReady(
        totalVideoCount = 10,
        selectedVideoCount = 5,
        selectedDayThumbnails = emptyList(),
        recapStartDate = MockDataRecapCreateCustom.startDate,
        recapEndDate = MockDataRecapCreateCustom.endDate,
        onStartDateSelected = {},
        onEndDateSelected = {},
        recapName = "",
        onRecapNameChange = {},
        canSave = false,
        onSaveClick = {},
    )
}
