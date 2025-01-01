package com.lukeneedham.videodiary.ui.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.navigation.Router
import com.lukeneedham.videodiary.ui.theme.VideoDiaryTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Root(
    share: (ShareRequest) -> Unit,
    setOrientation: (Orientation) -> Unit,
) {
    val viewModel: RootViewModel = koinViewModel()

    val hasSetupCompleted = viewModel.hasSetupCompleted
    val orientationState = viewModel.orientationState

    LaunchedEffect(orientationState) {
        when (orientationState) {
            RootOrientationState.Loading,
            RootOrientationState.NeedsSetup -> {
                // No-op
            }

            is RootOrientationState.Ready -> {
                setOrientation(orientationState.orientation)
            }
        }
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .systemBarsPadding()
    ) {
        VideoDiaryTheme {
            if (hasSetupCompleted != null && orientationState !is RootOrientationState.Loading) {
                val needsSetup =
                    !hasSetupCompleted || orientationState is RootOrientationState.NeedsSetup

                Router(
                    needsSetup = needsSetup,
                    share = share,
                    setOrientation = setOrientation,
                )
            }
        }
    }
}