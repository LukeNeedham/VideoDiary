package com.lukeneedham.videodiary.data.mapper

import java.time.LocalDate

class ThumbnailFileNameMapper {
    fun dateToName(date: LocalDate): String {
        val components = listOf(
            date.year,
            date.monthValue,
            date.dayOfMonth,
        )
        val name = components.joinToString(separator)
        return name + extension
    }

    companion object {
        private const val separator = "-"
        private const val extension = ".jpg"
    }
}
