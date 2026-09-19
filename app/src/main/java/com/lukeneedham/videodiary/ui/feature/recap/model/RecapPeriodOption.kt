package com.lukeneedham.videodiary.ui.feature.recap.model

import java.time.LocalDate

/** A candidate whole month/week/year the user can jump straight into a recap of. */
data class RecapPeriodOption(
    val label: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    /** A compact rendering of [startDate]..[endDate], e.g. "10 - 30 September". */
    val dateRangeText: String,
    val suggestedName: String,
    val hasVideos: Boolean,
)
