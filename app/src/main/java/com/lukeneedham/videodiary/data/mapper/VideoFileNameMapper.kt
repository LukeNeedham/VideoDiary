package com.lukeneedham.videodiary.data.mapper

import java.time.LocalDate

class VideoFileNameMapper {
    fun dateToName(date: LocalDate): String {
        val components = listOf(
            date.year,
            date.monthValue,
            date.dayOfMonth,
        )
        val name = components.joinToString(separator)
        return name + extension
    }

    fun nameToDate(name: String): LocalDate {
        val nameWithoutExtension = name.removeSuffix(extension)
        val components = nameWithoutExtension.split(separator)
        fun get(index: Int) = components[index].toInt()
        val year = get(0)
        val month = get(1)
        val day = get(2)
        return LocalDate.of(year, month, day)
    }

    companion object {
        private const val separator = "-"
        private const val extension = ".mp4"
    }
}