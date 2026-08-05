package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import android.os.Parcelable
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportRequest
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class NormalPage : Parcelable {
    data object Calendar : NormalPage()
    data class RecordVideo(val date: LocalDate) : NormalPage()
    data class CheckVideo(
        val date: LocalDate,
        val videoContentUri: Uri,
    ) : NormalPage()

    data object ExportHub : NormalPage()
    data object ExportDiaryCreate : NormalPage()
    data class ExportDiaryProgress(val exportRequest: ExportRequest) : NormalPage()
    data class ExportDiaryView(val exportedVideo: ExportedVideo) : NormalPage()

    data object Debug : NormalPage()
    data object CrashLog : NormalPage()
}
