package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import android.util.Size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lukeneedham.videodiary.data.persistence.model.OrientationPersistence
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import com.lukeneedham.videodiary.domain.model.Orientation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** Persists the settings configured by the user when first setting up the diary */
class SettingsDao(
    private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    /** The selected camera resolution, which is the resolution used to select the camera */
    private val resolutionKey = stringPreferencesKey("resolution")

    /** The selected camera rotation degrees, which is one of: 0, 90, 180, 270 */
    private val resolutionRotationKey = intPreferencesKey("resolutionRotation")

    /** The selected video orientation, which also drives the app orientation */
    private val videoOrientationKey = stringPreferencesKey("videoOrientation")

    /** The selected duration of each video in seconds */
    private val videoDurationMillisKey = longPreferencesKey("videoDurationMillis")

    suspend fun setResolution(resolution: Size) {
        val components = listOf(resolution.width, resolution.height)
        val string = components.joinToString(resolutionSeparator)
        updatePrefs {
            set(resolutionKey, string)
        }
    }

    suspend fun getResolution(): Size? {
        val string = getPrefs()[resolutionKey] ?: return null
        val components = string.split(resolutionSeparator)
        fun get(index: Int) = components[index].toInt()
        return Size(get(0), get(1))
    }

    suspend fun setResolutionRotation(rotation: CameraResolutionRotation) {
        updatePrefs {
            set(resolutionRotationKey, rotation.degrees)
        }
    }

    suspend fun getResolutionRotation(): CameraResolutionRotation? {
        val degrees = getPrefs()[resolutionRotationKey] ?: return null
        return CameraResolutionRotation.fromDegrees(degrees)
    }

    suspend fun setOrientation(orientation: Orientation) {
        val persistence = when (orientation) {
            Orientation.Portrait -> OrientationPersistence.Portrait
            Orientation.Landscape -> OrientationPersistence.Landscape
        }
        val id = persistence.persistenceId
        updatePrefs {
            set(videoOrientationKey, id)
        }
    }

    fun getOrientationFlow(): Flow<Orientation?> {
        return context.dataStore.data.map { prefs ->
            val id = prefs[videoOrientationKey] ?: return@map null
            val persistence = OrientationPersistence.fromId(id)
            when (persistence) {
                OrientationPersistence.Portrait -> Orientation.Portrait
                OrientationPersistence.Landscape -> Orientation.Landscape
            }
        }
    }

    suspend fun setVideoDuration(duration: Duration) {
        val millis = duration.inWholeMilliseconds
        updatePrefs {
            set(videoDurationMillisKey, millis)
        }
    }

    suspend fun getVideoDuration(): Duration? {
        val millis = getPrefs()[videoDurationMillisKey] ?: return null
        return millis.milliseconds
    }

    private suspend fun getPrefs() = context.dataStore.data.first()

    private suspend fun updatePrefs(block: MutablePreferences.() -> Unit) {
        context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                block()
            }
        }
    }

    companion object {
        private val resolutionSeparator = "x"
    }
}