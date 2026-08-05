package com.lukeneedham.videodiary.ui.feature.setup.intro

import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AccentRecord

data class SetupIntroSlide(
    val iconRes: Int,
    val accentColor: Color,
    val title: String,
    val description: String,
    /** Whether [iconRes] should be tinted with [accentColor], or drawn using its own colors */
    val tintIcon: Boolean = true,
)

val setupIntroSlides = listOf(
    SetupIntroSlide(
        iconRes = R.mipmap.ic_launcher_round,
        accentColor = AccentHighlight,
        title = "Welcome to your Video Diary",
        description = "Record your life, one short video a day. Here's how it works...",
        tintIcon = false,
    ),
    SetupIntroSlide(
        iconRes = R.drawable.movie,
        accentColor = AccentRecord,
        title = "One video a day",
        description = "Each day you can record one short video - a snippet of your day. " +
            "Don't miss your chance!",
    ),
    SetupIntroSlide(
        iconRes = R.drawable.calendar_today,
        accentColor = AccentAccept,
        title = "Relive any day",
        description = "Hop back in time, or scroll through the calendar, to see what your " +
            "past self was up to.",
    ),
    SetupIntroSlide(
        iconRes = R.drawable.share,
        accentColor = AccentHighlight,
        title = "Export your story",
        description = "At any point, export your diary into a single video - a full recap " +
            "of your life so far!",
    ),
)
