package com.lukeneedham.videodiary.ui.feature.setup.intro

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.navigation.setup.SetupProgress
import com.lukeneedham.videodiary.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupIntroPage(
    onContinue: () -> Unit,
) {
    val slides = setupIntroSlides
    val pagerState = rememberPagerState { slides.size }
    val coroutineScope = rememberCoroutineScope()

    val isLastSlide by remember { derivedStateOf { pagerState.currentPage == slides.lastIndex } }

    fun goToSlide(index: Int) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(index)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = false,
            onBack = {},
            endContent = {
                if (!isLastSlide) {
                    Text(
                        text = "Skip",
                        color = Color.White,
                        modifier = Modifier
                            .clickable { goToSlide(slides.lastIndex) }
                            .padding(horizontal = 15.dp),
                    )
                }
            },
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            SetupIntroSlideContent(slide = slides[page])
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            PageIndicator(
                pageCount = SetupProgress.TOTAL_PAGE_COUNT,
                currentPageIndex = pagerState.currentPage,
                color = Color.Black,
            )
        }

        Button(
            text = "Next",
            onClick = {
                if (isLastSlide) onContinue() else goToSlide(pagerState.currentPage + 1)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
        )
    }
}

@Composable
private fun SetupIntroSlideContent(
    slide: SetupIntroSlide,
    modifier: Modifier = Modifier,
) {
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
                .background(color = slide.accentColor.copy(alpha = 0.15f), shape = CircleShape),
        ) {
            Image(
                painter = painterResource(slide.iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(slide.accentColor),
                modifier = Modifier.size(70.dp),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = slide.title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = Typography.Size.big,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = slide.description,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(2f))
    }
}

@Preview
@Composable
internal fun PreviewSetupIntroPage() {
    SetupIntroPage(
        onContinue = {},
    )
}

@Preview
@Composable
private fun PreviewSetupIntroSlideContent() {
    SetupIntroSlideContent(
        slide = setupIntroSlides.first(),
    )
}
