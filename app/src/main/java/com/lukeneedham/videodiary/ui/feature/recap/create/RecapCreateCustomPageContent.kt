package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.SubpageToolbar
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AppBackground
import java.time.LocalDate

@Composable
fun RecapCreateCustomPageContent(
    canGoBack: Boolean,
    onBack: () -> Unit,
    totalVideoCount: Int?,
    selectedVideoCount: Int?,
    selectedDayThumbnails: List<RecapDayThumbnail>?,
    diaryStartDate: LocalDate?,
    recapStartDate: LocalDate?,
    recapEndDate: LocalDate?,
    onStartDateSelected: (LocalDate?) -> Unit,
    onEndDateSelected: (LocalDate?) -> Unit,
    recapName: String,
    onRecapNameChange: (String) -> Unit,
    canSave: Boolean,
    onSaveClick: () -> Unit,
) {
    ToolbarPageLayout(
        bottomBar = {
            SubpageToolbar(
                canGoBack = canGoBack,
                onBack = onBack,
                centerContent = {
                    Button(
                        text = "Create",
                        onClick = onSaveClick,
                        enabled = canSave && totalVideoCount != null && totalVideoCount > 0,
                        backgroundColor = AccentHighlight,
                        foregroundColor = Color.Black,
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                    )
                },
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
        ) {
            if (recapStartDate == null || recapEndDate == null || totalVideoCount == null || diaryStartDate == null) {
                CircularProgressIndicator(
                    color = AccentHighlight,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                RecapCreateCustomPageReady(
                    totalVideoCount = totalVideoCount,
                    selectedVideoCount = selectedVideoCount,
                    selectedDayThumbnails = selectedDayThumbnails,
                    recapStartDate = recapStartDate,
                    recapEndDate = recapEndDate,
                    onStartDateSelected = onStartDateSelected,
                    onEndDateSelected = onEndDateSelected,
                    recapName = recapName,
                    onRecapNameChange = onRecapNameChange,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewRecapCreateCustomPageContent() {
    Box(
        modifier = Modifier.background(AppBackground)
    ) {
        RecapCreateCustomPageContent(
            canGoBack = true,
            onBack = {},
            totalVideoCount = 10,
            selectedVideoCount = 5,
            selectedDayThumbnails = emptyList(),
            recapStartDate = MockDataRecapCreateCustom.startDate,
            recapEndDate = MockDataRecapCreateCustom.endDate,
            diaryStartDate = MockDataRecapCreateCustom.diaryStartDate,
            onStartDateSelected = {},
            onEndDateSelected = {},
            recapName = "",
            onRecapNameChange = {},
            canSave = false,
            onSaveClick = {},
        )
    }
}
