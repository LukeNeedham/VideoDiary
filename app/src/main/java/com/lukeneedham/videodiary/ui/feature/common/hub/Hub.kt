package com.lukeneedham.videodiary.ui.feature.common.hub

import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.R

/**
 * A top-level section of the app. Each hub has a root page, and may have sub pages of its own.
 * Hubs are switched between via the hub switcher.
 */
enum class Hub(
    val title: String,
    val description: String,
    val iconRes: Int,
) {
    Calendar(title = "Calendar", description = "Record and browse your daily videos", iconRes = R.drawable.calendar_today),
    Recap(title = "Recap", description = "Make and view recap videos", iconRes = R.drawable.movie),
    Settings(title = "Settings", description = "App settings and developer tools", iconRes = R.drawable.settings);

    companion object {
        /** The hubs that can be chosen in the hub switcher in this build. */
        val available: List<Hub>
            get() = entries.filter { it != Settings || BuildConfig.DEBUG }
    }
}
