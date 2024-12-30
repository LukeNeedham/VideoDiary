package com.lukeneedham.videodiary.data.repository

import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.util.ext.aspectRatio

class VideoResolutionRepository(
    private val settingsDao: SettingsDao,
) {
    suspend fun getAspectRatio(): Float? {
        val resolution = settingsDao.getResolution() ?: return null
        val rotation = settingsDao.getResolutionRotation() ?: return null
        val rotatedResolution = rotation.rotate(resolution)
        return rotatedResolution.aspectRatio()
    }
}