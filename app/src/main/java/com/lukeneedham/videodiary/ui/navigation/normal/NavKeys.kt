package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import android.os.Parcelable
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class AppNavKey : Parcelable {
    /** The hub this page is the root page of, or null if this is a sub page. */
    val hub: Hub?
        get() = when (this) {
            is CalendarNavKey -> Hub.Calendar
            is RecapHubNavKey -> Hub.Recap
            is SettingsHubNavKey -> Hub.Settings
            else -> null
        }
}

data object CalendarNavKey : AppNavKey()
data class RecordVideoNavKey(val date: LocalDate) : AppNavKey()
data class CheckVideoNavKey(
    val date: LocalDate,
    val videoContentUri: Uri,
) : AppNavKey()

data object RecapHubNavKey : AppNavKey()
data class RecapCreatePeriodListNavKey(val periodType: RecapPeriodType) : AppNavKey()
data object RecapCreateCustomNavKey : AppNavKey()
data class RecapViewNavKey(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val name: String,
    val savedRecapId: String?,
) : AppNavKey()

data object SettingsHubNavKey : AppNavKey()

/** The app's own debug page. */
data object AppDebugNavKey : AppNavKey()

/** The shared library's debug page, opened from [AppDebugNavKey]. */
data object DebugNavKey : AppNavKey()

data object LogsNavKey : AppNavKey()
data object CrashesNavKey : AppNavKey()
