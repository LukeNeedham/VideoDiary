package com.lukeneedham.videodiary.domain.model

import java.io.File

data class ShareRequest(
    val title: String,
    val text: String,
    val video: File,
)
