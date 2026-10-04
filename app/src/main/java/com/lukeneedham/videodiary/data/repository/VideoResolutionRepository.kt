package com.lukeneedham.videodiary.data.repository

import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.util.ext.aspectRatio

/**
 * The video aspect ratio is only ever written by the setup flow, so once it's known it's cached in
 * memory (this must be a singleton) and read synchronously via [cachedAspectRatio]. Call
 * [clearCache] whenever the resolution settings are written.
 */
class VideoResolutionRepository(
    private val settingsDao: SettingsDao,
) {
    @Volatile
    var cachedAspectRatio: Float? = null
        private set

    suspend fun getAspectRatio(): Float? {
        cachedAspectRatio?.let { return it }
        val resolution = settingsDao.getResolution() ?: return null
        val rotation = settingsDao.getResolutionRotation() ?: return null
        val rotatedResolution = rotation.rotate(resolution)
        // Unset (null) is never cached - it's expected to be set later, by the setup flow
        return rotatedResolution.aspectRatio().also { cachedAspectRatio = it }
    }

    fun clearCache() {
        cachedAspectRatio = null
    }
}
