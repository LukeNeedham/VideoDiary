package com.lukeneedham.videodiary.ui.feature.permissions

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.Typography

internal fun iconFor(permission: RequiredPermission): Int = when (permission.permission) {
    Manifest.permission.CAMERA -> R.drawable.camera
    Manifest.permission.RECORD_AUDIO -> R.drawable.mic
    else -> R.drawable.movie
}

@Composable
internal fun RequestPermissionSlideContent(
    permission: RequiredPermission,
    isGranted: Boolean,
    modifier: Modifier = Modifier,
) {
    val accentColor = if (isGranted) AccentAccept else AccentHighlight

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .background(color = accentColor.copy(alpha = 0.15f), shape = CircleShape),
        ) {
            Image(
                painter = painterResource(iconFor(permission)),
                contentDescription = null,
                colorFilter = ColorFilter.tint(accentColor),
                modifier = Modifier.size(70.dp),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = permission.displayName,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = Typography.Size.big,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = permission.description,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (isGranted) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.tick),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(AccentAccept),
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Granted",
                    color = AccentAccept,
                )
            }
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview
@Composable
private fun PreviewRequestPermissionSlideContent() {
    RequestPermissionSlideContent(
        permission = RequiredPermissions.permissions.first(),
        isGranted = false,
    )
}
