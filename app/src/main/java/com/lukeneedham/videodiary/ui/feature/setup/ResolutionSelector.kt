package com.lukeneedham.videodiary.ui.feature.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun ResolutionSelector(
    currentResolutionIndex: Int,
    setCurrentResolutionIndex: (Int) -> Unit,
    currentResolutionName: String?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        @Composable
        fun Button(
            iconRes: Int,
            contentDescription: String,
            onClick: () -> Unit,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        onClick()
                    }
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Button(
            iconRes = R.drawable.chevron_left,
            contentDescription = "Previous",
        ) {
            setCurrentResolutionIndex(currentResolutionIndex - 1)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            if (currentResolutionName != null) {
                Text(
                    text = currentResolutionName,
                    color = Color.Black,
                )
            }
        }

        Button(
            iconRes = R.drawable.chevron_right,
            contentDescription = "Next",
        ) {
            setCurrentResolutionIndex(currentResolutionIndex + 1)
        }
    }
}

@Preview
@Composable
internal fun PreviewResolutionSelector() {
    ResolutionSelector(
        currentResolutionIndex = 0,
        setCurrentResolutionIndex = {},
        currentResolutionName = "100x200",
    )
}