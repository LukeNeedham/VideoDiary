package com.lukeneedham.videodiary.ui.feature.setup.resolution

import android.util.Size
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraInput
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraQualityEffect
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.setup.SetupStepHeader

@Composable
fun SetupPageContent(
    resolutions: List<Size>,
    currentResolutionIndex: Int,
    setCurrentResolutionIndex: (Int) -> Unit,
    rotation: CameraResolutionRotation?,
    onResolutionLoaded: (resolution: Size, isMissing: Boolean, rotation: CameraResolutionRotation) -> Unit,
) {
    val currentResolution = resolutions.getOrNull(currentResolutionIndex)
    var currentQuality: Quality? by remember { mutableStateOf(null) }

    val currentResolutionName = run {
        if (currentResolution == null) return@run null
        val rotationLocal = rotation ?: return@run null
        val rotatedResolution = rotationLocal.rotate(currentResolution)
        "${rotatedResolution.width}x${rotatedResolution.height}"
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SetupStepHeader(
            iconRes = R.drawable.camera,
            title = "Select video quality",
            description = "Use the arrows on the preview to browse each available resolution.",
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ResolutionSelector(currentResolutionName = currentResolutionName)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                PageIndicator(
                    pageCount = resolutions.size,
                    currentPageIndex = currentResolutionIndex,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CameraQualityEffect(currentResolution) {
                currentQuality = it
            }

            val currentQualityLocal = currentQuality
            if (currentQualityLocal != null && currentResolution != null) {
                // Dummy recorder and video capture -
                // just used to configure the preview with the selected quality
                val recorder = remember(currentQualityLocal) {
                    Recorder.Builder()
                        .setQualitySelector(
                            QualitySelector.from(currentQualityLocal)
                        )
                        .build()
                }
                val videoCapture = remember(recorder) {
                    VideoCapture.Builder(recorder).build()
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    GlassIconButton(
                        iconRes = R.drawable.chevron_left,
                        contentDescription = "Previous resolution",
                        onClick = { setCurrentResolutionIndex(currentResolutionIndex - 1) },
                        modifier = Modifier.padding(8.dp),
                    )

                    CameraInput(
                        videoCapture = videoCapture,
                        currentResolution = currentResolution,
                        onResolutionLoaded = onResolutionLoaded,
                        canZoom = false,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    GlassIconButton(
                        iconRes = R.drawable.chevron_right,
                        contentDescription = "Next resolution",
                        onClick = { setCurrentResolutionIndex(currentResolutionIndex + 1) },
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewSetupPageContent() {
    SetupPageContent(
        resolutions = emptyList(),
        currentResolutionIndex = 0,
        setCurrentResolutionIndex = {},
        rotation = null,
        onResolutionLoaded = { _, _, _ -> },
    )
}
