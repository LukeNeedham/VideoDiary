package com.lukeneedham.videodiary.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.SavedRecap
import com.lukeneedham.videodiary.ui.feature.recap.hub.RecapHubPageContent
import com.lukeneedham.videodiary.ui.feature.recap.hub.SavedRecapWithThumbnails
import java.time.LocalDate

/** Rendered by CI to docs/screenshots/<position in order.txt>-RecapHubPage.png: the saved recaps. */
@Preview
@Composable
private fun RecapHubPageScreenshot() {
    RecapHubPageContent(
        savedRecaps = listOf(
            SavedRecapWithThumbnails(
                recap = SavedRecap(
                    id = "1",
                    name = "Summer 2024",
                    startDate = LocalDate.of(2024, 6, 1),
                    endDate = LocalDate.of(2024, 8, 31),
                ),
                dayVideoCount = 45,
                thumbnailFiles = emptyList(),
            ),
            SavedRecapWithThumbnails(
                recap = SavedRecap(
                    id = "2",
                    name = "Holiday Trip",
                    startDate = LocalDate.of(2024, 12, 20),
                    endDate = LocalDate.of(2025, 1, 5),
                ),
                dayVideoCount = 12,
                thumbnailFiles = emptyList(),
            ),
        ),
        videoAspectRatio = 9f / 16f,
        onMenuClick = {},
        onCreateRecapClick = {},
        onRecapClick = {},
        onDeleteClick = {},
    )
}
