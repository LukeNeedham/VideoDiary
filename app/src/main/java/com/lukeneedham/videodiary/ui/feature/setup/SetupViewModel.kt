package com.lukeneedham.videodiary.ui.feature.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.android.PermissionChecker
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

/** The state of the whole onboarding flow that its pages share: which permissions are granted, and whether it has a storage step. */
class SetupViewModel(
    private val permissionChecker: PermissionChecker,
    videosDao: VideosDao,
) : ViewModel() {
    /**
     * Whether there is an SD card to choose. Read once, when onboarding starts: the storage page is only part of
     * onboarding if this is true, and the pages must not change part way through.
     */
    val isRemovableStorageAvailable: Boolean = videosDao.isRemovableStorageAvailable()

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
