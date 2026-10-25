package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import android.os.Parcelable
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/** The destinations of the main app flow, one per page: [Calendar] is `CalendarPage`, and so on. */
@Parcelize
sealed interface Route : Parcelable {
    /** The hub this page is the root page of, or null if this is a sub page. */
    val hub: Hub?
        get() = when (this) {
            is Calendar -> Hub.Calendar
            is RecapHub -> Hub.Recap
            is SettingsHub -> Hub.Settings
            else -> null
        }

    data object Calendar : Route
    data class RecordVideo(val date: LocalDate) : Route
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : Route

    data object RecapHub : Route
    data class RecapCreatePeriodList(val periodType: RecapPeriodType) : Route
    data object RecapCreateCustom : Route
    data class RecapView(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val name: String,
        val savedRecapId: String?,
    ) : Route

    data object SettingsHub : Route

    /** The app's own debug page. */
    data object AppDebug : Route

    /** The shared library's debug page, opened from [AppDebug]. */
    data object Debug : Route

    data object Logs : Route
    data object Crashes : Route
}
