package com.lukeneedham.videodiary.ui.feature.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.util.DpUtil

private val TrackColor = Color(0xFFEFEFF2)

/** Renders a row of options, of which the user can pick exactly one, as a modern sliding-pill toggle */
@Composable
fun <T> SingleOptionSelector(
    options: List<T>,
    selectedOption: T,
    setSelectedOption: (T) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = AccentHighlight,
    optionContent: @Composable (option: T, isSelected: Boolean) -> Unit,
) {
    val trackShape = RoundedCornerShape(20.dp)
    val pillShape = RoundedCornerShape(16.dp)

    /** Map of option to it's position in the page */
    val optionToBounds = remember {
        mutableStateMapOf<T, Rect>()
    }

    val selectedBounds = optionToBounds[selectedOption] ?: Rect.Zero

    val selectedTargetWidth = selectedBounds.width
    val selectedTargetWidthDp = DpUtil.fromPxToDp(px = selectedTargetWidth)
    val selectedWidthDp by animateDpAsState(
        targetValue = selectedTargetWidthDp,
        label = "selectedWidthDp"
    )

    val selectedTargetXOffset = selectedBounds.left.toInt()
    val selectedXOffset by animateIntAsState(
        targetValue = selectedTargetXOffset,
        label = "selectedXOffset"
    )
    val selectedIntOffset = IntOffset(x = selectedXOffset, y = 0)

    Box(
        modifier = modifier
            .background(color = TrackColor, shape = trackShape)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
        ) {
            // The selection background pill which moves to highlight the currently selected option
            Box(
                modifier = Modifier
                    .offset { selectedIntOffset }
                    .fillMaxHeight()
                    .width(selectedWidthDp)
                    .shadow(elevation = 3.dp, shape = pillShape)
                    .background(color = accentColor, shape = pillShape)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                options.forEach { option ->
                    val isSelected = selectedOption == option
                    Box(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .widthIn(min = 60.dp)
                            .onGloballyPositioned {
                                optionToBounds[option] = it.boundsInParent()
                            }
                            .clickable { setSelectedOption(option) }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        optionContent(option, isSelected)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSingleItemRowSelector() {
    SingleOptionSelector(
        options = listOf("a", "eniohoa ndosroei", "cnaear amroie mar"),
        selectedOption = "a",
        setSelectedOption = {},
        optionContent = { option, isSelected ->
            val color = if (isSelected) Color.White else Color.DarkGray
            Text(text = option, color = color)
        },
        modifier = Modifier.fillMaxWidth()
    )
}
