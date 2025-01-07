package com.lukeneedham.videodiary.ui.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionsPage
import com.lukeneedham.videodiary.ui.navigation.normal.NormalRouter
import com.lukeneedham.videodiary.ui.navigation.setup.SetupRouter
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import com.lukeneedham.videodiary.ui.theme.VideoDiaryTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Root(
    share: (ShareRequest) -> Unit,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permissions: String) -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
) {
    val viewModel: RootViewModel = koinViewModel()

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
            // Black system bars
            .background(Color.Black)
            .systemBarsPadding()
            .background(Color.White)
    ) {
        VideoDiaryTheme {
            when (viewModel.state) {
                RootState.Loading -> {
                    CircularProgressIndicator()
                }

                RootState.NeedsPermissions -> {
                    RequestPermissionsPage(
                        viewModel = koinViewModel(),
                        requestPermission = requestPermission,
                        onContinue = viewModel::onPermissionsAcquired,
                        permissionResultListenerHolder = permissionResultListenerHolder,
                    )
                }

                RootState.NeedsSetup -> {
                    SetupRouter(
                        onSetupComplete = viewModel::onSetupComplete,
                        setOrientation = setOrientation,
                    )
                }

                RootState.Ready -> {
                    NormalRouter(
                        share = share,
                    )
                }
            }
        }
    }
}