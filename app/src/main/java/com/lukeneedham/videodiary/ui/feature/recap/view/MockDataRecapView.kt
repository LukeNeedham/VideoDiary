package com.lukeneedham.videodiary.ui.feature.recap.view

import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import java.io.File
import java.time.LocalDate

object MockDataRecapView {
    val name = "Spring Trip"
    val startDate = LocalDate.of(2024, 3, 27)
    val endDate = LocalDate.of(2024, 4, 20)
    val videoFiles = listOf(File("mock1"), File("mock2"), File("mock3"))
    val dayThumbnails = listOf(
        RecapDayThumbnail(date = LocalDate.of(2024, 3, 27), thumbnailFile = null),
        RecapDayThumbnail(date = LocalDate.of(2024, 3, 28), thumbnailFile = null),
        RecapDayThumbnail(date = LocalDate.of(2024, 3, 29), thumbnailFile = null),
    )
}
