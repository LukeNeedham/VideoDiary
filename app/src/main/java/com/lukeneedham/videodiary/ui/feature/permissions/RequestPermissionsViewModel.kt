package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.android.PermissionChecker
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

class RequestPermissionsViewModel(
    private val permissionChecker: PermissionChecker,
) : ViewModel() {
    val requiredPermissions = RequiredPermissions.permissions

    var acquiredPermissions: List<String> by mutableStateOf(loadAcquiredPermissions())
        private set

    fun onPermissionResult() {
        acquiredPermissions = loadAcquiredPermissions()
    }

    private fun loadAcquiredPermissions() = requiredPermissions
        .map { it.permission }
        .filter { permissionChecker.hasPermission(it) }
}