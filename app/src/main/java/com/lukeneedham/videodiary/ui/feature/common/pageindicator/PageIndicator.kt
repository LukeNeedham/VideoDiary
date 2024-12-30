package com.lukeneedham.videodiary.ui.feature.common.pageindicator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPageIndex: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier
    ) {
        repeat(pageCount) { index ->
            val isCurrent = index == currentPageIndex
            val dotColor = if (isCurrent) color else color.copy(0.3f)
            PageIndicatorDot(color = dotColor, modifier = Modifier.size(10.dp))
        }
    }
}

@Preview
@Composable
private fun PreviewPageIndicator() {
    PageIndicator(
        pageCount = 6,
        currentPageIndex = 2,
        color = Color.Black,
    )
}
