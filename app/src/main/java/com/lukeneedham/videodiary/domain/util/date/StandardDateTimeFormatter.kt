package com.lukeneedham.videodiary.domain.util.date

import java.time.format.DateTimeFormatter

object StandardDateTimeFormatter {
    /** Used throughout the UI as the standard way of formatting dates */
    val date = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val time = DateTimeFormatter.ofPattern("HH:ss")

    /** Day-of-month with no leading zero, e.g. "1".."31" */
    val dayOfMonth = DateTimeFormatter.ofPattern("d")

    /** Short month name, e.g. "Jan" */
    val monthShort = DateTimeFormatter.ofPattern("MMM")

    /** Full month name, e.g. "January" */
    val monthFull = DateTimeFormatter.ofPattern("MMMM")

    /** Full year, e.g. "2024" */
    val year = DateTimeFormatter.ofPattern("yyyy")
}