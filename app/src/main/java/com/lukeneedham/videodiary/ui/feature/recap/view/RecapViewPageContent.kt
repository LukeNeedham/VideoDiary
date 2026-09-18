package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.VideoControlsRow
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoQueuePlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoToolbarLayout
import com.lukeneedham.videodiary.ui.feature.recap.create.component.RecapThumbnailRow
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.theme.Typography
import java.io.File
import java.time.LocalDate

@Composable
fun RecapViewPageContent(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate,
    videoAspectRatio: Float?,
    videoFiles: List<File>,
    dayThumbnails: List<RecapDayThumbnail>,
    isSaved: Boolean,
    onToggleSavedClick: () -> Unit,
    includeDateStamp: Boolean,
    setIncludeDateStamp: (Boolean) -> Unit,
    onExportClick: () -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val controller = remember {
        VideoPlayerController().apply {
            isVolumeOn = true
        }
    }

    VideoToolbarLayout(
        videoAspectRatio = videoAspectRatio,
        topOverlay = {
            RecapViewToolbar(
                name = name,
                startDate = startDate,
                endDate = endDate,
                canGoBack = canGoBack,
                onBack = onBack,
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                if (dayThumbnails.isNotEmpty()) {
                    RecapThumbnailRow(
                        thumbnails = dayThumbnails,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                VideoControlsRow {
                    val muteIcon =
                        if (controller.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
                    GlassIconButton(
                        iconRes = muteIcon,
                        contentDescription = "Toggle sound",
                        onClick = { controller.toggleVolumeOn() },
                    )

                    val isPlaying = !controller.isTogglePaused
                    val playIcon = if (isPlaying) R.drawable.pause else R.drawable.play
                    GlassIconButton(
                        iconRes = playIcon,
                        contentDescription = "Play/pause",
                        onClick = { controller.toggleIsPlaying() },
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = includeDateStamp,
                        onCheckedChange = setIncludeDateStamp,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White,
                            checkmarkColor = Color.Black,
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Include date stamp when exporting to a single video",
                        color = Color.White,
                        fontSize = Typography.Size.extraSmall,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        text = if (isSaved) "Remove from saved recaps" else "Save recap",
                        onClick = onToggleSavedClick,
                        backgroundColor = Color.White,
                        foregroundColor = Color.Black,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        text = "Export to single video",
                        onClick = onExportClick,
                        backgroundColor = Color.White,
                        foregroundColor = Color.Black,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
    ) { aspectRatio ->
        VideoQueuePlayer(
            videoFiles = videoFiles,
            aspectRatio = aspectRatio,
            controller = controller,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun RecapViewToolbar(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate,
    canGoBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(modifier = Modifier.size(50.dp)) {
            if (canGoBack) {
                GlassIconButton(
                    iconRes = R.drawable.back,
                    contentDescription = "Back",
                    onClick = onBack,
                )
            }
        }

        val start = startDate.format(StandardDateTimeFormatter.date)
        val end = endDate.format(StandardDateTimeFormatter.date)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = name,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = Typography.Size.medium,
            )
            Text(
                text = "$start to $end",
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                fontSize = Typography.Size.extraSmall,
            )
        }

        Box(modifier = Modifier.size(50.dp))
    }
}

@Preview
@Composable
private fun PreviewPortrait() {
    Box(
        modifier = Modifier
            .height(3000.dp)
            .width(1000.dp)
    ) {
        RecapViewPageContent(
            name = MockDataRecapView.name,
            startDate = MockDataRecapView.startDate,
            endDate = MockDataRecapView.endDate,
            videoAspectRatio = 1f,
            videoFiles = MockDataRecapView.videoFiles,
            dayThumbnails = MockDataRecapView.dayThumbnails,
            isSaved = false,
            onToggleSavedClick = {},
            includeDateStamp = true,
            setIncludeDateStamp = {},
            onExportClick = {},
            canGoBack = true,
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSaved() {
    Box(
        modifier = Modifier
            .height(3000.dp)
            .width(1000.dp)
    ) {
        RecapViewPageContent(
            name = MockDataRecapView.name,
            startDate = MockDataRecapView.startDate,
            endDate = MockDataRecapView.endDate,
            videoAspectRatio = 1f,
            videoFiles = MockDataRecapView.videoFiles,
            dayThumbnails = MockDataRecapView.dayThumbnails,
            isSaved = true,
            onToggleSavedClick = {},
            includeDateStamp = true,
            setIncludeDateStamp = {},
            onExportClick = {},
            canGoBack = true,
            onBack = {},
        )
    }
}
