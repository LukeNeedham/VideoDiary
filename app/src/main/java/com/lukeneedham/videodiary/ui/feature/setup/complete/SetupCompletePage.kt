package com.lukeneedham.videodiary.ui.feature.setup.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.Typography

/** The final page of the onboarding flow, shown right before the user enters the app. */
@Composable
fun SetupCompletePage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .background(color = AccentAccept.copy(alpha = 0.15f), shape = CircleShape),
        ) {
            Image(
                painter = painterResource(R.drawable.tick),
                contentDescription = null,
                colorFilter = ColorFilter.tint(AccentAccept),
                modifier = Modifier.size(70.dp),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "You're all set!",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = Typography.Size.big,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Time to start capturing your daily story - one video at a time.",
            color = Color.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview
@Composable
private fun PreviewSetupCompletePage() {
    SetupCompletePage()
}
