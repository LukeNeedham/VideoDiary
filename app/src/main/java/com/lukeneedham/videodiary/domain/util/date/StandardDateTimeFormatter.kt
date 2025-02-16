package com.lukeneedham.videodiary.domain.util.date

import java.time.format.DateTimeFormatter

object StandardDateTimeFormatter {
    /** Used throughout the UI as the standard way of formatting dates */
    val date = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val time = DateTimeFormatter.ofPattern("HH:ss")
}