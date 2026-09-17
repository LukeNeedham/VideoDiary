package com.lukeneedham.videodiary.ui.feature.record.check.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassSurface
import com.lukeneedham.videodiary.ui.theme.Typography

/**
 * A two-line "glass" button used to pick which of the two compared videos to keep: a small
 * "CHOOSE" label above the larger [label] naming the video (e.g. "EXISTING"/"NEW").
 */
@Composable
fun ChooseVideoButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassSurface(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 10.dp, horizontal = 8.dp),
        ) {
            Text(
                text = "CHOOSE",
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.SemiBold,
                fontSize = Typography.Size.extraSmall,
            )
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = Typography.Size.big,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewChooseVideoButton() {
    ChooseVideoButton(
        label = "EXISTING",
        onClick = {},
    )
}
