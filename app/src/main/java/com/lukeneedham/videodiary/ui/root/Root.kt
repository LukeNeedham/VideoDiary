package com.lukeneedham.videodiary.ui.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionsPage
import com.lukeneedham.videodiary.ui.feature.setup.SetupPage
import com.lukeneedham.videodiary.ui.navigation.normal.AppNavigation
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import com.lukeneedham.videodiary.ui.theme.AppTheme
import com.lukeneedham.videodiary.ui.theme.ProvideAppDebugSettings
import org.koin.compose.koinInject

@Composable
fun Root(
    share: (ShareRequest) -> Unit,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permissions: String) -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
) {
    val coordinator: RootCoordinator = koinInject()

    // Whenever the app comes back to the foreground, check the permissions again: a revoked one blocks the normal UI
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, coordinator) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) coordinator.onResume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val orientationState = coordinator.orientationState

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
            .fillMaxSize()
            // Black status bar. The bottom nav bar inset is each screen's own responsibility
            // (not handled here), so screens that need the full window height - like
            // RecapShareSheet's ModalBottomSheetLayout - aren't shrunk by space they don't use.
            .background(Color.Black)
            .statusBarsPadding()
            .background(Color.White)
    ) {
        AppTheme {
            // Every app wraps its content in ProvideDebugSettings, even without a debug setting to read.
            ProvideAppDebugSettings {
                when (coordinator.state) {
                    RootState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    RootState.NeedsPermissions -> {
                        RequestPermissionsPage(
                            requestPermission = requestPermission,
                            onContinue = coordinator::onPermissionsAcquired,
                            permissionResultListenerHolder = permissionResultListenerHolder,
                        )
                    }

                    RootState.NeedsSetup -> {
                        SetupPage(
                            onSetupComplete = coordinator::onSetupComplete,
                            setOrientation = setOrientation,
                            requestPermission = requestPermission,
                            permissionResultListenerHolder = permissionResultListenerHolder,
                            onPermissionsAcquired = coordinator::onPermissionsAcquired,
                        )
                    }

                    RootState.Ready -> {
                        AppNavigation(
                            share = share,
                        )
                    }
                }
            }
        }
    }
}