package com.lukeneedham.videodiary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.ui.navigation.Router
import com.lukeneedham.videodiary.ui.theme.VideoDiaryTheme
import org.koin.compose.koinInject

@Composable
fun Root() {
    val viewModel: RootViewModel = koinInject()

    val hasSetupCompleted = viewModel.hasSetupCompleted

    Box(
        modifier = Modifier
            .background(Color.White)
            .systemBarsPadding()
    ) {
        VideoDiaryTheme {
            if (hasSetupCompleted != null) {
                Router(
                    hasSetupCompleted = hasSetupCompleted,
                )
            }
        }
    }
}