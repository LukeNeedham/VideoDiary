package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import android.util.Size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import kotlinx.coroutines.flow.first
import java.time.LocalDate

/** Persists the settings configured by the user when first setting up the diary */
class SettingsDao(
    private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    /** The selected camera resolution, which is the resolution used to select the camera */
    private val resolutionKey = stringPreferencesKey("resolution")

    /** The selected camera rotation degrees, which is one of: 0, 90, 180, 270 */
    private val resolutionRotationKey = intPreferencesKey("resolutionRotation")

    /** The date on which the diary was started */
    private val startDateKey = stringPreferencesKey("startDate")

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

    suspend fun setStartDate(startDate: LocalDate) {
        val components = listOf(startDate.year, startDate.monthValue, startDate.dayOfMonth)
        val string = components.joinToString(startDateSeparator)
        updatePrefs {
            set(startDateKey, string)
        }
    }

    suspend fun getStartDate(): LocalDate? {
        val startDateString = getPrefs()[startDateKey] ?: return null
        val components = startDateString.split(startDateSeparator)
        fun get(index: Int) = components[index].toInt()
        return LocalDate.of(get(0), get(1), get(2))
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
        private val startDateSeparator = "-"
    }
}