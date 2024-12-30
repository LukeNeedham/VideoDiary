package com.lukeneedham.videodiary.ui.navigation

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class Page : Parcelable {
    data object Setup : Page()
    data object Calendar : Page()
    data object RecordVideo : Page()
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : Page()
}