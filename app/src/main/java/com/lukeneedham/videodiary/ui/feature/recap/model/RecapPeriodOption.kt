package com.lukeneedham.videodiary.ui.feature.recap.model

import java.time.LocalDate

/** A candidate whole month/week/year the user can jump straight into a recap of. */
data class RecapPeriodOption(
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val suggestedName: String,
    val hasVideos: Boolean,
)
