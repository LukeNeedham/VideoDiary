package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun RequestPermission(
    permission: RequiredPermission,
    isProvided: Boolean,
    onRequest: (permission: String) -> Unit,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = permission.displayName,
                    color = Color.Black,
                    fontSize = Typography.Size.big,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = permission.description,
                    color = Color.Gray,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                contentAlignment = Alignment.CenterEnd,
                // Fixed width so text doesnt resize when state changes
                modifier = Modifier.width(100.dp)
            ) {
                if (isProvided) {
                    Image(
                        painter = painterResource(R.drawable.tick),
                        contentDescription = "Done",
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Button(
                        text = "Request",
                        onClick = {
                            onRequest(permission.permission)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewRequestPermission() {
    RequestPermission(
        permission = RequiredPermissions.permissions.first(),
        isProvided = false,
        onRequest = {},
    )
}