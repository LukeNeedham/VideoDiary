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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
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
    val contentAlpha = if (option.hasVideos) 1f else 0.4f
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppSurfaceVariant.copy(alpha = contentAlpha))
            .let {
                if (option.hasVideos) it.clickable(onClick = onClick) else it
            }
            .padding(20.dp)
    ) {
        Text(
            text = option.label,
            color = Color.White.copy(alpha = contentAlpha),
            fontSize = Typography.Size.medium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        val start = option.startDate.format(StandardDateTimeFormatter.date)
        val end = option.endDate.format(StandardDateTimeFormatter.date)
        Text(
            text = "$start to $end",
            color = Color.White.copy(alpha = 0.6f * contentAlpha),
            fontSize = Typography.Size.extraSmall,
        )
        if (!option.hasVideos) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "No videos recorded in this period",
                color = Color.White.copy(alpha = 0.6f * contentAlpha),
                fontSize = Typography.Size.extraSmall,
            )
        }
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
                suggestedName = "March 2024 recap",
                hasVideos = true,
            ),
            RecapPeriodOption(
                label = "February 2024",
                startDate = LocalDate.of(2024, 2, 1),
                endDate = LocalDate.of(2024, 2, 29),
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
