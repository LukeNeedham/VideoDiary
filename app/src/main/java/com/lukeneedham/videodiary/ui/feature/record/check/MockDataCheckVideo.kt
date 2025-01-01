package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.core.net.toUri
import com.lukeneedham.videodiary.domain.model.Video

object MockDataCheckVideo {
    val video = Video.MediaStore(uri = "".toUri())
}