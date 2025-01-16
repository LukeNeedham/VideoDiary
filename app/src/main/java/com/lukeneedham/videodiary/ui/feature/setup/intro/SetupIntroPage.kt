package com.lukeneedham.videodiary.ui.feature.setup.intro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun SetupIntroPage(
    onContinue: () -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val nextButtonText = "Next"

    val description = """
        Record your life in one short video a day!
        
        Here's how it works:
    """.trimIndent()

    val bullets = listOf(
        "Each day you can record one short video - a snippet of your day. Don't miss your chance! \uD83C\uDFA5",
        "Hop back in time or scroll through the calendar to see what your past self was up to \uD83C\uDF9E\uFE0F",
        "At any point export your diary to a single video, for a full recap of your life so far! \uD83C\uDFAC",
    )

    val cta = "Click the '${nextButtonText}' button below to start setting up your diary..."

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = canGoBack, onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Welcome to your personal Video Diary!",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = Typography.Size.big,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = description,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            bullets.forEach {
                SetupIntroBullet(it)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = cta,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Button(
            text = nextButtonText,
            onClick = {
                onContinue()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
    }
}

@Preview
@Composable
internal fun PreviewSetupIntroPage() {
    SetupIntroPage(
        onContinue = {},
        canGoBack = true,
        onBack = {},
    )
}