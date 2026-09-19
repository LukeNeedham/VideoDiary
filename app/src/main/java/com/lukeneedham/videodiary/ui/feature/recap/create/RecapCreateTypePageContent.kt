package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun RecapCreateTypePageContent(
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateMonthClick: () -> Unit,
    onCreateWeekClick: () -> Unit,
    onCreateYearClick: () -> Unit,
    onCreateCustomClick: () -> Unit,
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CreateRecapTypeRow(
                title = "Month recap",
                description = "Every day recorded in a chosen month",
                onClick = onCreateMonthClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
            CreateRecapTypeRow(
                title = "Week recap",
                description = "Every day recorded in a chosen week",
                onClick = onCreateWeekClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
            CreateRecapTypeRow(
                title = "Year recap",
                description = "Every day recorded in a chosen year",
                onClick = onCreateYearClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
            CreateRecapTypeRow(
                title = "Custom recap",
                description = "Pick your own dates and give it a name",
                onClick = onCreateCustomClick,
            )
        }
    }
}

@Composable
private fun CreateRecapTypeRow(
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppSurfaceVariant)
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.add),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = Typography.Size.medium,
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRecapCreateTypePageContent() {
    RecapCreateTypePageContent(
        canGoBack = true,
        onBack = {},
        onCreateMonthClick = {},
        onCreateWeekClick = {},
        onCreateYearClick = {},
        onCreateCustomClick = {},
    )
}
