package com.lukeneedham.videodiary.domain.model

import java.time.Instant

data class CrashLog(
    val timestamp: Instant,
    val stackTrace: String,
)
