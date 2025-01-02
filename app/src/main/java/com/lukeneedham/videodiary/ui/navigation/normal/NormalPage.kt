package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class NormalPage : Parcelable {
    data object Calendar : NormalPage()
    data object RecordVideo : NormalPage()
    data object ExportDiary : NormalPage()
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : NormalPage()
}