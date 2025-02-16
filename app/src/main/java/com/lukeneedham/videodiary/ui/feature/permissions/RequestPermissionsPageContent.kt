package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

@Composable
fun RequestPermissionsPageContent(
    requestPermission: (permission: String) -> Unit,
    onContinue: () -> Unit,
    acquiredPermissions: List<String>,
    requiredPermissions: List<RequiredPermission>,
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = "The following permissions are required for this app to work!",
            textAlign = TextAlign.Center,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            requiredPermissions.forEach { permission ->
                RequestPermission(
                    permission = permission,
                    isProvided = permission.permission in acquiredPermissions,
                    onRequest = requestPermission,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        val canContinue = requiredPermissions.all {
            it.permission in acquiredPermissions
        }
        Button(
            text = "Continue",
            enabled = canContinue,
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
internal fun PreviewRequestPermissionsPageContent() {
    RequestPermissionsPageContent(
        requestPermission = {},
        onContinue = {},
        acquiredPermissions = emptyList(),
        requiredPermissions = RequiredPermissions.permissions,
    )
}