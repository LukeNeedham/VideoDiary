package com.lukeneedham.videodiary.ui.feature.recap.create

import java.time.LocalDate

data class RecapCreatedArgs(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val name: String,
    val savedRecapId: String,
)
