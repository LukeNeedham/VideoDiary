package com.lukeneedham.videodiary.ui.feature.setup.orientation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.SingleOptionSelector

@Composable
fun SetupSelectOrientationPageContent(
    options: List<Orientation>,
    selectedOption: Orientation,
    setSelectedOption: (Orientation) -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Select video (and app) orientation"
        )
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
internal fun PreviewSetupSelectOrientationPageContent() {
    SetupSelectOrientationPageContent(
        options = Orientation.entries,
        selectedOption = Orientation.Portrait,
        setSelectedOption = {},
        onContinue = {},
    )
}