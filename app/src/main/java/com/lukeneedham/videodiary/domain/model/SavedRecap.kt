package com.lukeneedham.videodiary.domain.model

import java.time.LocalDate

data class SavedRecap(
    val id: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
