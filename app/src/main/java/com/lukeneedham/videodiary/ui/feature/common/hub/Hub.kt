package com.lukeneedham.videodiary.ui.feature.common.hub

import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.R

/**
 * A top-level section of the app. Each hub has a root page, and may have sub pages of its own.
 * Hubs are switched between via the hub switcher.
 */
enum class Hub(
    val title: String,
    val iconRes: Int,
) {
    Calendar(title = "Calendar", iconRes = R.drawable.calendar_today),
    Recap(title = "Recap", iconRes = R.drawable.movie),
    Debug(title = "Debug", iconRes = R.drawable.bug);

    companion object {
        /** The hubs that can be chosen in the hub switcher in this build. */
        val available: List<Hub>
            get() = entries.filter { it != Debug || BuildConfig.DEBUG }
    }
}
