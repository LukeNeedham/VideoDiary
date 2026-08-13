package com.lukeneedham.videodiary.ui.feature.setup.duration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.IntPicker
import com.lukeneedham.videodiary.ui.feature.setup.SetupStepHeader

@Composable
fun SelectVideoDurationPageContent(
    seconds: Int,
    setSeconds: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SetupStepHeader(
            iconRes = R.drawable.movie,
            title = "Select the duration of each video",
            description = "How many seconds should each daily recording last?",
            modifier = Modifier.padding(top = 20.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IntPicker(
                    value = seconds, setValue = setSeconds,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "seconds")
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewSelectVideoDurationPageContent() {
    SelectVideoDurationPageContent(
        seconds = 1,
        setSeconds = {},
    )
}
