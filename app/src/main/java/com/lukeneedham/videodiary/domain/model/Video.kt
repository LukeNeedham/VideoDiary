package com.lukeneedham.videodiary.domain.model

import android.net.Uri
import java.io.File

sealed interface Video {
    data class MediaStore(val uri: Uri) : Video
    data class PersistedFile(val file: File) : Video
}