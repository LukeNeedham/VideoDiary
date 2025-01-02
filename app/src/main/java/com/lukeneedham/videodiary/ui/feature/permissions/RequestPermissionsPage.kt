package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder

@Composable
fun RequestPermissionsPage(
    viewModel: RequestPermissionsViewModel,
    requestPermission: (permission: String) -> Unit,
    onContinue: () -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
) {
    LaunchedEffect(permissionResultListenerHolder, viewModel) {
        permissionResultListenerHolder.onPermissionResult = viewModel::onPermissionResult
    }

    RequestPermissionsPageContent(
        requestPermission = requestPermission,
        onContinue = onContinue,
        acquiredPermissions = viewModel.acquiredPermissions,
        requiredPermissions = viewModel.requiredPermissions,
    )
}