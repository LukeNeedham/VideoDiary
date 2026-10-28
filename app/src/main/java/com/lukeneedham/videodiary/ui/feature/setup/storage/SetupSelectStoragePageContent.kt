package com.lukeneedham.videodiary.ui.feature.setup.storage

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import com.lukeneedham.videodiary.ui.feature.common.SingleOptionSelector
import com.lukeneedham.videodiary.ui.feature.setup.SetupStepHeader
import com.lukeneedham.videodiary.ui.theme.AccentRecord
import com.lukeneedham.videodiary.ui.theme.Typography
import com.lukeneedham.videodiary.ui.theme.AppTheme

@Composable
internal fun SetupSelectStoragePageContent(
    options: List<VideoStorageLocation>,
    selectedOption: VideoStorageLocation,
    setSelectedOption: (VideoStorageLocation) -> Unit,
    error: String?,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SetupStepHeader(
            iconRes = R.drawable.movie,
            title = "Where should your videos be stored?",
            description = "Videos can take up a lot of space. You can change this later in Settings.",
            modifier = Modifier.padding(top = 20.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SingleOptionSelector(
                    options = options,
                    selectedOption = selectedOption,
                    setSelectedOption = setSelectedOption,
                    modifier = Modifier.fillMaxWidth(),
                ) { option, isSelected ->
                    val text = when (option) {
                        VideoStorageLocation.Internal -> "Internal storage"
                        VideoStorageLocation.RemovableStorage -> "SD card"
                    }

                    val targetTextColor = if (isSelected) Color.White else Color.DarkGray
                    val textColor by animateColorAsState(
                        targetValue = targetTextColor,
                        label = "Animate storage text color"
                    )
                    Text(
                        text = text,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = textColor,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val explanation = when (selectedOption) {
                    VideoStorageLocation.Internal ->
                        "Your videos stay in the app's private storage on your phone."

                    VideoStorageLocation.RemovableStorage ->
                        "Your videos are kept in this app's folder on your SD card. " +
                            "The SD card must be in your phone to use the app, and the videos " +
                            "are deleted if the app is uninstalled."
                }
                Text(
                    text = explanation,
                    color = Color.Gray,
                    fontSize = Typography.Size.extraSmall,
                    textAlign = TextAlign.Center,
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = AccentRecord,
                        fontSize = Typography.Size.extraSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        SetupSelectStoragePageContent(
            options = listOf(VideoStorageLocation.Internal, VideoStorageLocation.RemovableStorage),
            selectedOption = VideoStorageLocation.RemovableStorage,
            setSelectedOption = {},
            error = null,
        )
    }
}
