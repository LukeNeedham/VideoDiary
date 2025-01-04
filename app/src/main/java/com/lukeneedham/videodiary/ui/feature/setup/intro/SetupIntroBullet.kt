package com.lukeneedham.videodiary.ui.feature.setup.intro

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SetupIntroBullet(
    text: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "•",
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
internal fun PreviewSetupIntroBullet() {
    SetupIntroBullet(
        text = "This is a really long piece of text. It is intended to be a good demonstration of this component, to show how it handles wrapping of text."
    )
}