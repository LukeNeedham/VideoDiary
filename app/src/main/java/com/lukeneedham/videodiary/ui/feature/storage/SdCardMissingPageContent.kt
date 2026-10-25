package com.lukeneedham.videodiary.ui.feature.storage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun SdCardMissingPageContent(
    canMockReinsert: Boolean,
    onMockReinsertClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(AccentHighlight.copy(alpha = 0.15f))
        ) {
            Image(
                painter = painterResource(R.drawable.info),
                contentDescription = null,
                colorFilter = ColorFilter.tint(AccentHighlight),
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Whoa there!",
            color = Color.White,
            fontSize = Typography.Size.big,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "To use this app, you need to reinsert your SD card. " +
                "Your diary videos are stored on it, so the app will open again as soon as " +
                "the card is back.",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = Typography.Size.medium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Lost your card? You can start over by clearing this app's data in " +
                "Android settings.",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = Typography.Size.extraSmall,
            textAlign = TextAlign.Center,
        )
        if (canMockReinsert) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                text = "Reinsert SD card (debug)",
                onClick = onMockReinsertClick,
                backgroundColor = AppSurfaceVariant,
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 10.dp,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSdCardMissingPageContent() {
    SdCardMissingPageContent(
        canMockReinsert = false,
        onMockReinsertClick = {},
    )
}

@Preview
@Composable
private fun PreviewSdCardMissingPageContentDebug() {
    SdCardMissingPageContent(
        canMockReinsert = true,
        onMockReinsertClick = {},
    )
}
