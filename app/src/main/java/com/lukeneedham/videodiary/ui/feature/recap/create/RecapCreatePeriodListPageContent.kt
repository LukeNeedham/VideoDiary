package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            Text(
                text = "No videos yet - record some diary entries first.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.small,
                modifier = Modifier.padding(20.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(options, key = { it.label + it.startDate }) { option ->
                    RecapPeriodOptionRow(option = option, onClick = { onOptionClick(option) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RecapPeriodOptionRow(
    option: RecapPeriodOption,
    onClick: () -> Unit,
) {
    val hasVideos = option.hasVideos
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            // alpha must precede background/content so the whole card fades as one unit,
            // rather than compounding with the text colors' own alpha below.
            .alpha(if (hasVideos) 1f else 0.65f)
            .background(AppSurfaceVariant)
            .let { if (hasVideos) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 20.dp, vertical = if (hasVideos) 20.dp else 12.dp)
    ) {
        Text(
            text = option.label,
            color = Color.White,
            fontSize = if (hasVideos) Typography.Size.medium else Typography.Size.small,
        )
        Spacer(modifier = Modifier.height(4.dp))
        val subtitle = if (hasVideos) {
            option.dateRangeText
        } else {
            "${option.dateRangeText} · No videos"
        }
        Text(
            text = subtitle,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = Typography.Size.extraSmall,
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
                label = "March 2024",
                startDate = LocalDate.of(2024, 3, 1),
                endDate = LocalDate.of(2024, 3, 31),
                dateRangeText = "1 - 31 March",
                suggestedName = "March 2024 recap",
                hasVideos = true,
            ),
            RecapPeriodOption(
                label = "February 2024",
                startDate = LocalDate.of(2024, 2, 1),
                endDate = LocalDate.of(2024, 2, 29),
                dateRangeText = "1 - 29 February",
                suggestedName = "February 2024 recap",
                hasVideos = false,
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
        periodType = RecapPeriodType.WEEK,
        options = emptyList(),
        isLoaded = true,
        canGoBack = true,
        onBack = {},
        onOptionClick = {},
    )
}
