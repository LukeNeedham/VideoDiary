package com.lukeneedham.videodiary.ui.feature.recap.export

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.lukeneedham.videodiary.ui.feature.recap.export.model.RecapExportProgressState
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun RecapExportProgressPageContent(
    progressState: RecapExportProgressState,
    isCancelling: Boolean,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            when (progressState) {
                is RecapExportProgressState.InProgress -> {
                    Text(
                        text = "Preparing your recap...",
                        color = Color.Black,
                        fontSize = Typography.Size.medium,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LinearProgressIndicator(
                        progress = progressState.progressFraction,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        text = if (isCancelling) "Cancelling..." else "Cancel",
                        onClick = onCancelClick,
                        enabled = !isCancelling,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapExportProgressState.Failed -> {
                    Text(
                        text = "Something went wrong while preparing your recap: ${progressState.error}",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        text = "Back",
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewRecapExportProgressPageContentInProgress() {
    RecapExportProgressPageContent(
        progressState = MockDataRecapExportProgress.inProgress,
        isCancelling = false,
        onCancelClick = {},
        onBackClick = {},
    )
}

@Preview
@Composable
internal fun PreviewRecapExportProgressPageContentFailed() {
    RecapExportProgressPageContent(
        progressState = MockDataRecapExportProgress.failed,
        isCancelling = false,
        onCancelClick = {},
        onBackClick = {},
    )
}
