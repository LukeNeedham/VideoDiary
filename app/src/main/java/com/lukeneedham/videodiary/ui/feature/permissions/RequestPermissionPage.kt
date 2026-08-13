package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

/** One required permission, as a single page within the wider onboarding pager. */
@Composable
fun RequestPermissionPage(
    permission: RequiredPermission,
    isGranted: Boolean,
    requestPermission: (permission: String) -> Unit,
    pageIndex: Int,
    totalPageCount: Int,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = false,
            onBack = {},
        )

        RequestPermissionSlideContent(
            permission = permission,
            isGranted = isGranted,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            PageIndicator(
                pageCount = totalPageCount,
                currentPageIndex = pageIndex,
                color = Color.Black,
            )
        }

        // Reserve the button's height even when granted, so the page doesn't jump right before
        // the automatic transition to the next page.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .padding(15.dp),
        ) {
            if (!isGranted) {
                Button(
                    text = "Grant permission",
                    onClick = { requestPermission(permission.permission) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewRequestPermissionPage() {
    RequestPermissionPage(
        permission = RequiredPermissions.permissions.first(),
        isGranted = false,
        requestPermission = {},
        pageIndex = 4,
        totalPageCount = 7,
    )
}
