package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import android.os.Parcelable
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import kotlinx.parcelize.Parcelize
import java.io.File
import java.time.LocalDate

@Parcelize
sealed class NormalPage : Parcelable {
    data object Calendar : NormalPage()
    data object RecordVideo : NormalPage()
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : NormalPage()

    data object ExportDiaryCreate : NormalPage()
    data class ExportDiaryView(val exportedVideo: ExportedVideo) : NormalPage()
}