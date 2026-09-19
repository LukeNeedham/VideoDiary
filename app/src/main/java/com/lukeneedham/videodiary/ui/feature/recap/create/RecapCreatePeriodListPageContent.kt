package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.LocalDate

@Composable
fun RecapCreatePeriodListPageContent(
    periodType: RecapPeriodType,
    options: List<RecapPeriodOption>,
    isLoaded: Boolean,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onOptionClick: (RecapPeriodOption) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        GenericToolbar(
            canGoBack = canGoBack,
            onBack = onBack,
        )

        if (!isLoaded) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (options.isEmpty()) {
            NoPeriodsYet(periodType = periodType)
        } else {
            // `options` is oldest-first, laid out normally (oldest top-left, latest at the
            // bottom-right) so rows read left-to-right in chronological order. The grid's initial
            // scroll position is set to the last item so the page opens already scrolled to the
            // latest one, with older ones revealed by scrolling up.
            val gridState = rememberLazyGridState(
                initialFirstVisibleItemIndex = (options.size - 1).coerceAtLeast(0),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(options, key = { it.label + it.startDate }) { option ->
                    RecapPeriodOptionCard(option = option, onClick = { onOptionClick(option) })
                }
            }
        }
    }
}

@Composable
private fun NoPeriodsYet(periodType: RecapPeriodType) {
    val periodName = when (periodType) {
        RecapPeriodType.MONTH -> "month"
        RecapPeriodType.WEEK -> "week"
        RecapPeriodType.YEAR -> "year"
    }
    val whenClause = when (periodType) {
        RecapPeriodType.MONTH -> "the end of this calendar month"
        RecapPeriodType.WEEK -> "the end of this week (Sunday)"
        RecapPeriodType.YEAR -> "the end of this calendar year"
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No completed ${periodName}s yet",
                color = Color.White,
                fontSize = Typography.Size.medium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A $periodName only shows up here once it's fully over, so you'll see " +
                    "your first one after $whenClause.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun RecapPeriodOptionCard(
    option: RecapPeriodOption,
    onClick: () -> Unit,
) {
    val hasVideos = option.videoCount > 0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            // alpha must precede background/content so the whole card fades as one unit,
            // rather than compounding with the text colors' own alpha below.
            .alpha(if (hasVideos) 1f else 0.65f)
            .background(AppSurfaceVariant)
            .let { if (hasVideos) it.clickable(onClick = onClick) else it }
            .padding(16.dp)
    ) {
        Text(
            text = option.label,
            color = Color.White,
            fontSize = Typography.Size.medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        // The subtitle always reserves 2 lines - the date range, then the video count - so every
        // card is the same height regardless of its content, without hardcoding a dp height
        // (which wouldn't respect font scaling).
        val videoCountText = if (option.videoCount == 1) "1 video" else "${option.videoCount} videos"
        Text(
            text = "${option.dateRangeText}\n$videoCountText",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = Typography.Size.extraSmall,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun PreviewRecapCreatePeriodListPageContent() {
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

@Preview
@Composable
private fun PreviewRecapCreatePeriodListPageContentEmpty() {
    RecapCreatePeriodListPageContent(
        periodType = RecapPeriodType.YEAR,
        options = emptyList(),
        isLoaded = true,
        canGoBack = true,
        onBack = {},
        onOptionClick = {},
    )
}
