package com.lukeneedham.videodiary.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreatePeriodListPageContent
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.time.LocalDate

/** Rendered by CI to docs/screenshots/<position in order.txt>-RecapCreatePeriodListPage.png: choosing a month to make a recap of. */
@Preview
@Composable
private fun RecapCreatePeriodListPageScreenshot() {
    AppTheme {
        RecapCreatePeriodListPageContent(
            periodType = RecapPeriodType.MONTH,
            options = listOf(
                RecapPeriodOption(
                    label = "January 2024",
                    startDate = LocalDate.of(2024, 1, 1),
                    endDate = LocalDate.of(2024, 1, 31),
                    dateRangeText = "1 - 31 January",
                    suggestedName = "January 2024 recap",
                    videoCount = 12,
                ),
                RecapPeriodOption(
                    label = "February 2024",
                    startDate = LocalDate.of(2024, 2, 1),
                    endDate = LocalDate.of(2024, 2, 29),
                    dateRangeText = "1 - 29 February",
                    suggestedName = "February 2024 recap",
                    videoCount = 0,
                ),
                RecapPeriodOption(
                    label = "March 2024",
                    startDate = LocalDate.of(2024, 3, 1),
                    endDate = LocalDate.of(2024, 3, 31),
                    dateRangeText = "1 - 31 March",
                    suggestedName = "March 2024 recap",
                    videoCount = 1,
                ),
            ),
            isLoaded = true,
            canGoBack = true,
            onBack = {},
            onOptionClick = {},
        )
    }
}
