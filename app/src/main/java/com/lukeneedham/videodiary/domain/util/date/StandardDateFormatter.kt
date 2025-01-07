package com.lukeneedham.videodiary.domain.util.date

import java.time.format.DateTimeFormatter

object StandardDateFormatter {
    /** Used throughout the UI as the standard way of formatting dates */
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
}