package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun DebugPageContent(
    onFillWithMockDataClick: () -> Unit,
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

@Preview
@Composable
internal fun PreviewDebugPageContent() {
    DebugPageContent(
        onFillWithMockDataClick = {},
        canGoBack = true,
        onBack = {},
    )
}
