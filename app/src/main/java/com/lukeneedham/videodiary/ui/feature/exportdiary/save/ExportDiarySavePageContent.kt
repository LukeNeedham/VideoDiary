package com.lukeneedham.videodiary.ui.feature.exportdiary.save

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurface
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun ExportDiarySavePageContent(
    exportedVideo: ExportedVideo,
    name: String,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isSaving: Boolean,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        GenericToolbar(
            canGoBack = canGoBack,
            onBack = onBack,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Save Export",
                color = Color.White,
                fontSize = Typography.Size.big,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val start = exportedVideo.startDate.format(StandardDateTimeFormatter.date)
            val end = exportedVideo.endDate.format(StandardDateTimeFormatter.date)
            Text(
                text = "$start to $end · ${exportedVideo.dayVideoCount} videos",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = Typography.Size.small,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = {
                    Text(
                        text = "Export name",
                        color = Color.White.copy(alpha = 0.4f),
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = AppSurfaceVariant,
                    cursorColor = AccentHighlight,
                    focusedIndicatorColor = AccentHighlight,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                ),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSaveClick,
                enabled = name.trim().isNotEmpty() && !isSaving,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AccentHighlight,
                    contentColor = Color.Black,
                    disabledBackgroundColor = AppSurface,
                    disabledContentColor = Color.White.copy(alpha = 0.4f),
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewExportDiarySavePageContent() {
    Box(modifier = Modifier.background(AppBackground)) {
        ExportDiarySavePageContent(
            exportedVideo = com.lukeneedham.videodiary.ui.feature.exportdiary.view.MockDataExportDiaryView.exportedVideo,
            name = "My Holiday",
            onNameChange = {},
            onSaveClick = {},
            isSaving = false,
            canGoBack = true,
            onBack = {},
        )
    }
}
