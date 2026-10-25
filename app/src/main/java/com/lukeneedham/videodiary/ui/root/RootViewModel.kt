package com.lukeneedham.videodiary.ui.root

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.data.android.PermissionChecker
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RootViewModel(
    private val settingsDao: SettingsDao,
    private val permissionChecker: PermissionChecker,
    private val videosDao: VideosDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var isMissingPermissions: Boolean? by mutableStateOf(null)
    private var hasSetupCompleted: Boolean? by mutableStateOf(null)
    private var isStorageInitialised: Boolean by mutableStateOf(false)
    private var isSdCardMissing: Boolean by mutableStateOf(false)

    /** Debug builds only: whether the SD card missing page can pretend the card is put back */
    val canMockSdCardReinsert: Boolean get() = BuildConfig.DEBUG

    var orientationState: RootOrientationState by mutableStateOf(RootOrientationState.Loading)
        private set

    val state: RootState by derivedStateOf {
        val isMissingPermissions = isMissingPermissions ?: return@derivedStateOf RootState.Loading
        val hasSetupCompleted = hasSetupCompleted ?: return@derivedStateOf RootState.Loading
        if (!isStorageInitialised) return@derivedStateOf RootState.Loading

        // Nothing in the app works without the videos, so don't let the user get to any of it
        if (isSdCardMissing) return@derivedStateOf RootState.SdCardMissing

        // First-time (or interrupted) setup always goes through the setup wizard, which shows the
        // onboarding intro before requesting permissions part-way through.
        if (!hasSetupCompleted) return@derivedStateOf RootState.NeedsSetup

        // Returning user whose setup previously completed, but a permission has since been
        // revoked (e.g. from system settings) - re-request it directly, without the full wizard.
        if (isMissingPermissions) return@derivedStateOf RootState.NeedsPermissions

        val needsSetup = orientationState is RootOrientationState.NeedsSetup
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
        viewModelScope.launch {
            withContext(ioDispatcher) {
                videosDao.initialiseStorageLocation()
                videosDao.generateMissingThumbnails()
            }
            isStorageInitialised = true
        }
        viewModelScope.launch {
            videosDao.sdCardMissing.collect { isSdCardMissing = it }
        }
    }

    /**
     * Debug builds only: puts the fake SD card back, as if it had been reinserted.
     * Turns the fake SD card on if it was off, so this can never be a dead end.
     */
    fun mockSdCardReinsert() {
        viewModelScope.launch {
            settingsDao.setDebugFakeSdCardEnabled(true)
            settingsDao.setDebugFakeSdCardInserted(true)
            withContext(ioDispatcher) {
                videosDao.initialiseStorageLocation()
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