package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.navigation.setup.PagerAction
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

/** One required permission, as a single page within the wider onboarding pager. */
@Composable
fun RequestPermissionPage(
    permission: RequiredPermission,
    isGranted: Boolean,
    requestPermission: (permission: String) -> Unit,
    reportBottomAction: (PagerAction?) -> Unit,
) {
    SideEffect {
        reportBottomAction(
            if (isGranted) {
                null
            } else {
                PagerAction(
                    label = "Grant permission",
                    onClick = { requestPermission(permission.permission) },
                )
            }
        )
    }

    RequestPermissionSlideContent(
        permission = permission,
        isGranted = isGranted,
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
internal fun PreviewRequestPermissionPage() {
    RequestPermissionPage(
        permission = RequiredPermissions.permissions.first(),
        isGranted = false,
        requestPermission = {},
        reportBottomAction = {},
    )
}
