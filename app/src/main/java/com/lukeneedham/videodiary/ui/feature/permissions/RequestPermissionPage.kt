package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

/** One required permission, as a single page within the wider onboarding pager. */
@Composable
fun RequestPermissionPage(
    permission: RequiredPermission,
    isGranted: Boolean,
) {
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
    )
}
