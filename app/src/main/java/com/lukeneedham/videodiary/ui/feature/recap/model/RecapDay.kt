package com.lukeneedham.videodiary.ui.feature.recap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.time.LocalDate

@Parcelize
data class RecapDay(
    val date: LocalDate,
    val video: File,
) : Parcelable
