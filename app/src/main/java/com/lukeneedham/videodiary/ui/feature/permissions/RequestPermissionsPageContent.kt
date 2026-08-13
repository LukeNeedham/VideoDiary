package com.lukeneedham.videodiary.ui.feature.permissions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RequestPermissionsPageContent(
    requestPermission: (permission: String) -> Unit,
    onContinue: () -> Unit,
    acquiredPermissions: List<String>,
    requiredPermissions: List<RequiredPermission>,
) {
    // Pages beyond the first not-yet-granted permission aren't revealed yet, so the user can't
    // swipe past a permission without granting it.
    val firstMissingIndex = requiredPermissions.indexOfFirst { it.permission !in acquiredPermissions }
    val revealedPageCount = if (firstMissingIndex == -1) requiredPermissions.size else firstMissingIndex + 1

    val pagerState = rememberPagerState { revealedPageCount }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            val permission = requiredPermissions[page]
            RequestPermissionSlideContent(
                permission = permission,
                isGranted = permission.permission in acquiredPermissions,
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            PageIndicator(
                pageCount = requiredPermissions.size,
                currentPageIndex = pagerState.currentPage,
                color = Color.Black,
            )
        }

        val currentPermission = requiredPermissions.getOrNull(pagerState.currentPage)
        val isCurrentGranted = currentPermission != null && currentPermission.permission in acquiredPermissions
        val isLastPage = pagerState.currentPage == requiredPermissions.lastIndex
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .padding(15.dp),
        ) {
            Button(
                text = when {
                    !isCurrentGranted -> "Grant permission"
                    isLastPage -> "Continue"
                    else -> "Next"
                },
                onClick = {
                    when {
                        !isCurrentGranted -> currentPermission?.let { requestPermission(it.permission) }
                        isLastPage -> onContinue()
                        else -> coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
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
