package com.lukeneedham.videodiary.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import com.lukeneedham.videodiary.ui.theme.AppTheme
import com.lukeneedham.videodiary.ui.theme.ProvideAppDebugSettings
import org.koin.compose.viewmodel.koinViewModel

/** The root of the app: decides what the user sees (loading, permissions, setup, or the app) and themes it. */
@Composable
fun RootPage(
    share: (ShareRequest) -> Unit,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permissions: String) -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
) {
    val viewModel = koinViewModel<RootViewModel>()

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

    AppTheme {
        // Every app wraps its content in ProvideDebugSettings, even without a debug setting to read.
        ProvideAppDebugSettings {
            RootPageContent(
                state = viewModel.state,
                share = share,
                setOrientation = setOrientation,
                requestPermission = requestPermission,
                permissionResultListenerHolder = permissionResultListenerHolder,
                onPermissionsAcquired = viewModel::onPermissionsAcquired,
                onSetupComplete = viewModel::onSetupComplete,
            )
        }
    }
}
