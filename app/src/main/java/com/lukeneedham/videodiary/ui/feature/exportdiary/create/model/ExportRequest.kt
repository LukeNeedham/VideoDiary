package com.lukeneedham.videodiary.ui.feature.exportdiary.create.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class ExportRequest(
    val days: List<ExportDay>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val includeDateStamp: Boolean,
    val name: String,
) : Parcelable
