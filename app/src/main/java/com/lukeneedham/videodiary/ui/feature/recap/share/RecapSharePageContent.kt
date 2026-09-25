package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun RecapSharePageContent(
    state: RecapShareState,
    includeDateStamp: Boolean,
    onIncludeDateStampChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onRetryClick: () -> Unit,
    onShareClick: () -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        GenericToolbar(
            canGoBack = canGoBack,
            onBack = onBack,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(32.dp),
        ) {
            when (state) {
                RecapShareState.SelectingOptions -> {
                    Text(
                        text = "Share recap",
                        color = Color.White,
                        fontSize = Typography.Size.big,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = includeDateStamp,
                            onCheckedChange = onIncludeDateStampChange,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.White,
                                uncheckedColor = Color.White.copy(alpha = 0.4f),
                                checkmarkColor = Color.Black,
                            ),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Date stamp on each clip",
                            color = Color.White,
                            fontSize = Typography.Size.extraSmall,
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        text = "Create video",
                        onClick = onCreateClick,
                        backgroundColor = Color.White,
                        foregroundColor = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapShareState.InProgress -> {
                    Text(
                        text = "Preparing your recap...",
                        color = Color.White,
                        fontSize = Typography.Size.medium,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LinearProgressIndicator(
                        progress = state.progressFraction,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        text = "Cancel",
                        onClick = onCancelClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapShareState.Ready -> {
                    Text(
                        text = "Your recap is ready to share",
                        color = Color.White,
                        fontSize = Typography.Size.medium,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        text = "Share",
                        onClick = onShareClick,
                        backgroundColor = Color.White,
                        foregroundColor = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapShareState.Failed -> {
                    Text(
                        text = "Something went wrong while preparing your recap: ${state.error}",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        text = "Try again",
                        onClick = onRetryClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSelectingOptions() {
    RecapSharePageContent(
        state = RecapShareState.SelectingOptions,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        canGoBack = true,
        onBack = {},
    )
}

@Preview
@Composable
private fun PreviewInProgress() {
    RecapSharePageContent(
        state = MockDataRecapShare.inProgress,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        canGoBack = true,
        onBack = {},
    )
}

@Preview
@Composable
private fun PreviewReady() {
    RecapSharePageContent(
        state = MockDataRecapShare.ready,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        canGoBack = true,
        onBack = {},
    )
}

@Preview
@Composable
private fun PreviewFailed() {
    RecapSharePageContent(
        state = MockDataRecapShare.failed,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        canGoBack = true,
        onBack = {},
    )
}
