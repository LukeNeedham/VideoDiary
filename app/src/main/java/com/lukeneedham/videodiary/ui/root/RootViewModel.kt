package com.lukeneedham.videodiary.ui.root

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.android.PermissionChecker
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import kotlinx.coroutines.launch

class RootViewModel(
    private val settingsDao: SettingsDao,
    private val permissionChecker: PermissionChecker,
) : ViewModel() {
    private var isMissingPermissions: Boolean? by mutableStateOf(null)
    private var hasSetupCompleted: Boolean? by mutableStateOf(null)

    var orientationState: RootOrientationState by mutableStateOf(RootOrientationState.Loading)
        private set

    val state: RootState by derivedStateOf {
        val isMissingPermissions = isMissingPermissions ?: return@derivedStateOf RootState.Loading
        if (isMissingPermissions) return@derivedStateOf RootState.NeedsPermissions

        val hasSetupCompleted = hasSetupCompleted ?: return@derivedStateOf RootState.Loading

        val needsSetup =
            !hasSetupCompleted || orientationState is RootOrientationState.NeedsSetup
        if (needsSetup) return@derivedStateOf RootState.NeedsSetup
        RootState.Ready
    }

    init {
        isMissingPermissions = !hasAllRequiredPermissions()

        viewModelScope.launch {
            val requiredSetupData = listOf(
                settingsDao.getResolution(),
                settingsDao.getResolutionRotation(),
                settingsDao.getVideoDuration(),
            )
            hasSetupCompleted = requiredSetupData.all { it != null }
        }
        viewModelScope.launch {
            settingsDao.getOrientationFlow().collect { orientation ->
                orientationState = if (orientation == null) {
                    RootOrientationState.NeedsSetup
                } else {
                    RootOrientationState.Ready(orientation)
                }
            }
        }
    }

    fun onPermissionsAcquired() {
        isMissingPermissions = !hasAllRequiredPermissions()
    }

    fun onSetupComplete() {
        hasSetupCompleted = true
    }

    private fun hasAllRequiredPermissions(): Boolean {
        return RequiredPermissions.permissions.all { permission ->
            permissionChecker.hasPermission(permission.permission)
        }
    }
}