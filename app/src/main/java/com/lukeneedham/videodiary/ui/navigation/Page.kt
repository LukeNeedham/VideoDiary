package com.lukeneedham.videodiary.ui.navigation

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class Page : Parcelable {
    @Parcelize
    sealed class Setup : Page() {
        data object SelectOrientation : Setup()
        data object SelectResolution : Setup()
        data object SelectVideoDuration : Setup()
    }

    data object Calendar : Page()
    data object RecordVideo : Page()
    data object ExportDiary : Page()
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : Page()
}