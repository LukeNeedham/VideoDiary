package com.lukeneedham.videodiary.ui.feature.exportdiary.create.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.time.LocalDate

@Parcelize
data class ExportDay(
    val date: LocalDate,
    val video: File,
) : Parcelable