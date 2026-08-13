package com.lukeneedham.videodiary.ui.feature.setup.orientation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.ui.feature.common.SingleOptionSelector
import com.lukeneedham.videodiary.ui.feature.setup.SetupStepHeader

@Composable
fun SetupSelectOrientationPageContent(
    options: List<Orientation>,
    selectedOption: Orientation,
    setSelectedOption: (Orientation) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SetupStepHeader(
            iconRes = R.drawable.orientation,
            title = "Select video (and app) orientation",
            description = "Choose how you'll hold your phone to record - this also sets the app's own orientation.",
            modifier = Modifier.padding(top = 20.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
        ) {
            SingleOptionSelector(
                options = options,
                selectedOption = selectedOption,
                setSelectedOption = setSelectedOption,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            ) { option, isSelected ->
                val text = when (option) {
                    Orientation.Portrait -> "Portrait"
                    Orientation.Landscape -> "Landscape"
                }

                val targetTextColor = if (isSelected) Color.White else Color.Black
                val textColor by animateColorAsState(
                    targetValue = targetTextColor,
                    label = "Animate orientation text color"
                )
                Text(
                    text = text,
                    color = textColor,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewSetupSelectOrientationPageContent() {
    SetupSelectOrientationPageContent(
        options = Orientation.entries,
        selectedOption = Orientation.Portrait,
        setSelectedOption = {},
    )
}
