package com.lukeneedham.videodiary.ui.feature.setup.resolution

import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation

@Composable
fun SetupSelectResolutionPage(
    viewModel: SetupSelectResolutionViewModel,
    onContinue: () -> Unit,
    resolutions: List<Size>,
    currentResolutionIndex: Int,
    setCurrentResolutionIndex: (Int) -> Unit,
    rotation: CameraResolutionRotation?,
    onResolutionLoaded: (resolution: Size, isMissing: Boolean, rotation: CameraResolutionRotation) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SetupPageContent(
        resolutions = resolutions,
        currentResolutionIndex = currentResolutionIndex,
        setCurrentResolutionIndex = setCurrentResolutionIndex,
        rotation = rotation,
        onResolutionLoaded = onResolutionLoaded,
    )
}
