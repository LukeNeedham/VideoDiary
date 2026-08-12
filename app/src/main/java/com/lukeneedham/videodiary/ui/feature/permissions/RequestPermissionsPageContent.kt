package com.lukeneedham.videodiary.ui.feature.permissions

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.Typography
import kotlinx.coroutines.delay

private const val AUTO_CONTINUE_DELAY_MILLIS = 500L

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RequestPermissionsPageContent(
    requestPermission: (permission: String) -> Unit,
    onContinue: () -> Unit,
    acquiredPermissions: List<String>,
    requiredPermissions: List<RequiredPermission>,
    pageIndexOffset: Int = 0,
    totalPageCount: Int = requiredPermissions.size,
) {
    val pagerState = rememberPagerState { requiredPermissions.size }

    val currentPermission = requiredPermissions.getOrNull(pagerState.currentPage)
    val isCurrentGranted = currentPermission != null && currentPermission.permission in acquiredPermissions

    // Once the permission shown on the current page is granted, automatically move on - to the
    // next page, or out of the permission flow entirely once every permission is granted.
    LaunchedEffect(pagerState.currentPage, isCurrentGranted) {
        if (!isCurrentGranted) return@LaunchedEffect
        delay(AUTO_CONTINUE_DELAY_MILLIS)

        if (pagerState.currentPage < requiredPermissions.lastIndex) {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
            return@LaunchedEffect
        }

        val allGranted = requiredPermissions.all { it.permission in acquiredPermissions }
        if (allGranted) {
            onContinue()
        } else {
            val firstMissingIndex = requiredPermissions.indexOfFirst { it.permission !in acquiredPermissions }
            pagerState.animateScrollToPage(firstMissingIndex)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = false,
            onBack = {},
        )

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
                pageCount = totalPageCount,
                currentPageIndex = pageIndexOffset + pagerState.currentPage,
                color = Color.Black,
            )
        }

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

private fun iconFor(permission: RequiredPermission): Int = when (permission.permission) {
    Manifest.permission.CAMERA -> R.drawable.camera
    Manifest.permission.RECORD_AUDIO -> R.drawable.mic
    else -> R.drawable.movie
}

@Composable
private fun RequestPermissionSlideContent(
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
internal fun PreviewRequestPermissionsPageContent() {
    RequestPermissionsPageContent(
        requestPermission = {},
        onContinue = {},
        acquiredPermissions = emptyList(),
        requiredPermissions = RequiredPermissions.permissions,
    )
}
