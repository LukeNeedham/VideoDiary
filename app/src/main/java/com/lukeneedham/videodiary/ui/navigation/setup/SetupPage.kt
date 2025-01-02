package com.lukeneedham.videodiary.ui.navigation.setup

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed class SetupPage : Parcelable {
    data object SelectOrientation : SetupPage()
    data object SelectResolution : SetupPage()
    data object SelectVideoDuration : SetupPage()
}