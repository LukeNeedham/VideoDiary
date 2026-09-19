package com.lukeneedham.videodiary.ui.feature.recap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/** A request to stitch [days] into a single shareable video file. */
@Parcelize
data class RecapExportRequest(
    val days: List<RecapDay>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val includeDateStamp: Boolean,
    val name: String,
) : Parcelable
