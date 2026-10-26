@file:UseSerializers(LocalDateSerializer::class, UriSerializer::class)

package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import androidx.navigation3.runtime.NavKey
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

/** The destinations of the main app flow, one per page: [Calendar] is `CalendarPage`, and so on. */
@Serializable
sealed interface Route : NavKey {
    /** The hub this page is the root page of, or null if this is a sub page. */
    val hub: Hub?
        get() = when (this) {
            is Calendar -> Hub.Calendar
            is RecapHub -> Hub.Recap
            is SettingsHub -> Hub.Settings
            else -> null
        }

    @Serializable
    data object Calendar : Route

    @Serializable
    data class RecordVideo(val date: LocalDate) : Route

    @Serializable
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : Route

    @Serializable
    data object RecapHub : Route

    @Serializable
    data class RecapCreatePeriodList(val periodType: RecapPeriodType) : Route

    @Serializable
    data object RecapCreateCustom : Route

    @Serializable
    data class RecapView(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val name: String,
        val savedRecapId: String?,
    ) : Route

    @Serializable
    data object SettingsHub : Route

    /** The app's own debug page. */
    @Serializable
    data object AppDebug : Route

    /** The shared library's debug page, opened from [AppDebug]. */
    @Serializable
    data object Debug : Route

    @Serializable
    data object Logs : Route

    @Serializable
    data object Crashes : Route
}
