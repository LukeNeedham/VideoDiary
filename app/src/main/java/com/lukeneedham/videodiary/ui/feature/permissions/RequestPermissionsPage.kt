package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RequestPermissionsPage(
    requestPermission: (permission: String) -> Unit,
    onContinue: () -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
) {
    val viewModel = koinViewModel<RequestPermissionsViewModel>()

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
