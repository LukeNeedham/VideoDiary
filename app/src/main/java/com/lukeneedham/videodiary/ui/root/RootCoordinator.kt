package com.lukeneedham.videodiary.ui.root

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.data.android.PermissionChecker
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Decides what the app shows: the normal UI only when it can be used, otherwise the screen that deals with
 * what is missing (permissions, setup). It is not a ViewModel, because it belongs to no screen: [Root] always
 * renders, so there is one for the whole life of the app (a Koin `single`), with a scope of its own.
 */
class RootCoordinator(
    private val settingsDao: SettingsDao,
    private val permissionChecker: PermissionChecker,
    private val videosDao: VideosDao,
    private val ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) {
    private val scope = CoroutineScope(SupervisorJob() + mainDispatcher)

    private var isMissingPermissions: Boolean? by mutableStateOf(null)
    private var hasSetupCompleted: Boolean? by mutableStateOf(null)

    var orientationState: RootOrientationState by mutableStateOf(RootOrientationState.Loading)
        private set

    val state: RootState by derivedStateOf {
        val isMissingPermissions = isMissingPermissions ?: return@derivedStateOf RootState.Loading
        val hasSetupCompleted = hasSetupCompleted ?: return@derivedStateOf RootState.Loading

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

        scope.launch {
            val requiredSetupData = listOf(
                settingsDao.getResolution(),
                settingsDao.getResolutionRotation(),
                settingsDao.getVideoDuration(),
            )
            hasSetupCompleted = requiredSetupData.all { it != null }
        }
        scope.launch {
            settingsDao.getOrientationFlow().collect { orientation ->
                orientationState = if (orientation == null) {
                    RootOrientationState.NeedsSetup
                } else {
                    RootOrientationState.Ready(orientation)
                }
            }
        }
        scope.launch {
            withContext(ioDispatcher) {
                videosDao.generateMissingThumbnails()
            }
        }
    }

    fun onPermissionsAcquired() {
        isMissingPermissions = !hasAllRequiredPermissions()
    }

    /**
     * The app came back to the foreground. A permission may have been revoked meanwhile (from system
     * settings, say), so check again: if one is missing, [state] becomes [RootState.NeedsPermissions],
     * which takes over the UI until the user deals with it.
     */
    fun onResume() {
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