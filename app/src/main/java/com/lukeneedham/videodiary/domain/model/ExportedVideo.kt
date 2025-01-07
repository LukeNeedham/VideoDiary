package com.lukeneedham.videodiary.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.time.LocalDate

@Parcelize
data class ExportedVideo(
    val videoFile: File,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dayVideoCount: Int,
) : Parcelable