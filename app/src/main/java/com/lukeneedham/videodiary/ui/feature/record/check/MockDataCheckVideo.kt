package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.core.net.toUri
import com.lukeneedham.videodiary.domain.model.Video
import java.io.File

object MockDataCheckVideo {
    val newVideo = Video.MediaStore(uri = "".toUri())
    val existingVideo = Video.PersistedFile(file = File(""))
}
