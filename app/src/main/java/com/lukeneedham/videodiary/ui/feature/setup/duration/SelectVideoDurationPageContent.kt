package com.lukeneedham.videodiary.ui.feature.setup.duration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.IntPicker

@Composable
fun SelectVideoDurationPageContent(
    seconds: Int,
    setSeconds: (Int) -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = "Select the duration of each video")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IntPicker(
                value = seconds, setValue = setSeconds,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "seconds")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            text = "Next",
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
    }
}

@Preview
@Composable
internal fun PreviewSelectVideoDurationPageContent() {
    SelectVideoDurationPageContent(
        seconds = 1,
        setSeconds = {},
        onContinue = {},
    )
}