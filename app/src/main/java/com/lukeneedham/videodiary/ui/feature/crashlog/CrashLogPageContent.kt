package com.lukeneedham.videodiary.ui.feature.crashlog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.CrashLog
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

@Composable
fun CrashLogPageContent(
    crashLogs: List<CrashLog>,
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

        if (crashLogs.isEmpty()) {
            Text(
                text = "No crash logs recorded",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = Typography.Size.extraSmall,
                modifier = Modifier.padding(16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(crashLogs, key = { it.timestamp.toEpochMilli() }) { crashLog ->
                    CrashLogItem(crashLog)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CrashLogItem(crashLog: CrashLog) {
    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AppSurfaceVariant)
                .padding(16.dp)
        ) {
            Text(
                text = timestampFormatter.format(crashLog.timestamp.atZone(ZoneId.systemDefault())),
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = crashLog.stackTrace,
                color = Color.White,
                fontSize = Typography.Size.extraSmall,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmpty() {
    CrashLogPageContent(
        crashLogs = emptyList(),
        canGoBack = true,
        onBack = {},
    )
}

@Preview
@Composable
private fun PreviewWithItems() {
    CrashLogPageContent(
        crashLogs = listOf(
            CrashLog(
                timestamp = Instant.now(),
                stackTrace = "java.lang.RuntimeException: Something went wrong\n" +
                    "\tat com.lukeneedham.videodiary.Foo.bar(Foo.kt:10)",
            ),
        ),
        canGoBack = true,
        onBack = {},
    )
}
