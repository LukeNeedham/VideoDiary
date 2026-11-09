package com.lukeneedham.videodiary.ui.feature.setup.resolution

import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupSelectResolutionPage(
    onContinue: () -> Unit,
    /** Hands up what the primary button of the setup page does for this page. */
    registerSave: (save: () -> Unit) -> Unit,
    resolutions: List<Size>,
    currentResolutionIndex: Int,
    setCurrentResolutionIndex: (Int) -> Unit,
    rotation: CameraResolutionRotation?,
    onResolutionLoaded: (resolution: Size, isMissing: Boolean, rotation: CameraResolutionRotation) -> Unit,
) {
    val viewModel = koinViewModel<SetupSelectResolutionViewModel>()

    val currentResolution = resolutions.getOrNull(currentResolutionIndex)
    SideEffect {
        registerSave {
            if (currentResolution != null && rotation != null) {
                viewModel.saveSettings(currentResolution, rotation)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SetupSelectResolutionPageContent(
        resolutions = resolutions,
        currentResolutionIndex = currentResolutionIndex,
        setCurrentResolutionIndex = setCurrentResolutionIndex,
        rotation = rotation,
        onResolutionLoaded = onResolutionLoaded,
    )
}
