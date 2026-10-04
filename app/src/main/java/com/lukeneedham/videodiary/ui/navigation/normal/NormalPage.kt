package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import android.os.Parcelable
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class NormalPage : Parcelable {
    /** The hub this page is the root page of, or null if this is a sub page. */
    val hub: Hub?
        get() = when (this) {
            is Calendar -> Hub.Calendar
            is RecapHub -> Hub.Recap
            is SettingsHub -> Hub.Settings
            else -> null
        }

    data object Calendar : NormalPage()
    data class RecordVideo(val date: LocalDate) : NormalPage()
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : NormalPage()

    data object RecapHub : NormalPage()
    data class RecapCreatePeriodList(val periodType: RecapPeriodType) : NormalPage()
    data object RecapCreateCustom : NormalPage()
    data class RecapView(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val name: String,
        val savedRecapId: String?,
    ) : NormalPage()

    data object SettingsHub : NormalPage()
    data object Debug : NormalPage()
    data object CrashLog : NormalPage()
}
