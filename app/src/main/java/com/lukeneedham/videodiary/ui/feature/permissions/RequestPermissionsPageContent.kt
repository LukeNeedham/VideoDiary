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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import kotlinx.coroutines.delay

private const val AUTO_CONTINUE_DELAY_MILLIS = 500L

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

    // Once a permission is granted, automatically move on - but only once per permission, not
    // every time its page is revisited (e.g. by swiping back to it).
    var autoAdvancedPermissions by remember { mutableStateOf(emptySet<String>()) }

    // Keyed on settledPage (not currentPage) so this isn't cancelled and restarted by its own
    // in-flight scroll animation, or by the user's drag, mid-transition.
    val settledPage = pagerState.settledPage
    val settledPermission = requiredPermissions.getOrNull(settledPage)
    val isSettledPermissionNewlyGranted = settledPermission != null &&
        settledPermission.permission in acquiredPermissions &&
        settledPermission.permission !in autoAdvancedPermissions

    LaunchedEffect(settledPage, isSettledPermissionNewlyGranted) {
        if (!isSettledPermissionNewlyGranted) return@LaunchedEffect
        val permission = requireNotNull(settledPermission)
        delay(AUTO_CONTINUE_DELAY_MILLIS)
        autoAdvancedPermissions = autoAdvancedPermissions + permission.permission

        if (settledPage < requiredPermissions.lastIndex) {
            pagerState.animateScrollToPage(settledPage + 1)
        } else {
            onContinue()
        }
    }

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .padding(15.dp),
        ) {
            if (!isCurrentGranted) {
                Button(
                    text = "Grant permission",
                    onClick = {
                        currentPermission?.let { requestPermission(it.permission) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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
