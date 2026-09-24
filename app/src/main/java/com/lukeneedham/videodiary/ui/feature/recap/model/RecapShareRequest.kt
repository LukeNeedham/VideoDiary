package com.lukeneedham.videodiary.ui.feature.recap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/** A request to share [days] as a single stitched video, made on the recap share page. */
@Parcelize
data class RecapShareRequest(
    val days: List<RecapDay>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val name: String,
) : Parcelable
