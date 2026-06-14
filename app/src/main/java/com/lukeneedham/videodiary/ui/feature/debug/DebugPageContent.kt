package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun DebugPageContent(
    onFillWithMockDataClick: () -> Unit,
    allowRetakeForPastDays: Boolean,
    onAllowRetakeForPastDaysChange: (Boolean) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = canGoBack, onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Debug options",
                color = Color.Black,
                fontSize = Typography.Size.big,
            )

            Spacer(modifier = Modifier.height(20.dp))

            DebugOption(
                title = "Fill with mock data",
                description = "Overwrites all diary data with a random mix of mock videos, " +
                    "spanning the last 5 weeks",
                onClick = onFillWithMockDataClick,
            )

            DebugCheckboxOption(
                title = "Allow retake for past days",
                description = "Shows the Retake button for past days, allowing the video " +
                    "for that day to be re-recorded. Normally only today's video can be retaken",
                checked = allowRetakeForPastDays,
                onCheckedChange = onAllowRetakeForPastDaysChange,
            )
        }
    }
}

@Composable
private fun DebugOption(
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = Typography.Size.small,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            color = Color.Black,
            fontSize = Typography.Size.extraSmall,
        )
    }
}

@Composable
private fun DebugCheckboxOption(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 10.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = Color.Black,
                fontSize = Typography.Size.small,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = Color.Black,
                fontSize = Typography.Size.extraSmall,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewDebugPageContent() {
    DebugPageContent(
        onFillWithMockDataClick = {},
        allowRetakeForPastDays = false,
        onAllowRetakeForPastDaysChange = {},
        canGoBack = true,
        onBack = {},
    )
}
