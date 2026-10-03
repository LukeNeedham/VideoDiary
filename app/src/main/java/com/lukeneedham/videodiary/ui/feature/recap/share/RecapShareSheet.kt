package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.FlatIconButton
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

/**
 * The whole share flow (pick options, create the export, share it) as a single bottom sheet
 * docked over a recap's video, rather than a separate page - closing it just hides it, it doesn't
 * cancel an in-progress export or lose a finished one.
 */
@Composable
fun RecapShareSheet(
    visible: Boolean,
    state: RecapShareState,
    includeDateStamp: Boolean,
    onIncludeDateStampChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onRetryClick: () -> Unit,
    onShareClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(AppSurfaceVariant)
                    // Swallow taps so they don't fall through to the scrim's dismiss handler.
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    )
                    .navigationBarsPadding()
                    .padding(20.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    FlatIconButton(
                        iconRes = R.drawable.close,
                        contentDescription = "Close",
                        onClick = onDismiss,
                        selected = true,
                        size = 32.dp,
                        iconSize = 18.dp,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
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
    }
}

@Preview
@Composable
private fun PreviewSelectingOptions() {
    RecapShareSheet(
        visible = true,
        state = RecapShareState.SelectingOptions,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        onDismiss = {},
    )
}

@Preview
@Composable
private fun PreviewInProgress() {
    RecapShareSheet(
        visible = true,
        state = MockDataRecapShare.inProgress,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        onDismiss = {},
    )
}

@Preview
@Composable
private fun PreviewReady() {
    RecapShareSheet(
        visible = true,
        state = MockDataRecapShare.ready,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        onDismiss = {},
    )
}

@Preview
@Composable
private fun PreviewFailed() {
    RecapShareSheet(
        visible = true,
        state = MockDataRecapShare.failed,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onRetryClick = {},
        onShareClick = {},
        onDismiss = {},
    )
}
