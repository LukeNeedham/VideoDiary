package com.lukeneedham.videodiary.ui.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionsPage
import com.lukeneedham.videodiary.ui.feature.setup.SetupPage
import com.lukeneedham.videodiary.ui.navigation.normal.NormalRouter
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import com.lukeneedham.videodiary.ui.theme.AppTheme

/** What the root of the app shows for its [state]: a spinner, the permission page, the setup, or the app itself. */
@Composable
internal fun RootPageContent(
    state: RootState,
    share: (ShareRequest) -> Unit,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permissions: String) -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
    onPermissionsAcquired: () -> Unit,
    onSetupComplete: () -> Unit,
) {
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
        when (state) {
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
                    onContinue = onPermissionsAcquired,
                    permissionResultListenerHolder = permissionResultListenerHolder,
                )
            }

            RootState.NeedsSetup -> {
                SetupPage(
                    onSetupComplete = onSetupComplete,
                    setOrientation = setOrientation,
                    requestPermission = requestPermission,
                    permissionResultListenerHolder = permissionResultListenerHolder,
                    onPermissionsAcquired = onPermissionsAcquired,
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

@Preview
@Composable
private fun Preview() {
    AppTheme {
        RootPageContent(
            state = RootState.Loading,
            share = {},
            setOrientation = {},
            requestPermission = {},
            permissionResultListenerHolder = PermissionResultListenerHolder(),
            onPermissionsAcquired = {},
            onSetupComplete = {},
        )
    }
}
