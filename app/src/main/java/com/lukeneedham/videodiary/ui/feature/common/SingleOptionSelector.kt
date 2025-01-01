package com.lukeneedham.videodiary.ui.feature.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.util.DpUtil

/** Renders a row of options, of which the user can pick exactly one */
@Composable
fun <T> SingleOptionSelector(
    options: List<T>,
    selectedOption: T,
    setSelectedOption: (T) -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    optionContent: @Composable (option: T, isSelected: Boolean) -> Unit,
) {
    val shape = RoundedCornerShape(100)

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
            .height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
            .border(width = 2.dp, color = color, shape = shape)
            .clip(shape)
    ) {
        // The selection background pill which moves to highlight the currently selected option
        Box(
            modifier = Modifier
                .offset { selectedIntOffset }
                .background(color = color, shape = shape)
                .fillMaxHeight()
                .width(selectedWidthDp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option
                // Optionally pad the edge options more to deal with the curves of the pill border
                val isOnEdge = index == 0 || index == options.lastIndex
                val padding = 20.dp
                Box(
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .widthIn(min = 60.dp)
                        .onGloballyPositioned {
                            optionToBounds[option] = it.boundsInParent()
                        }
                        .clickable { setSelectedOption(option) }
                        .padding(horizontal = padding),
                    contentAlignment = Alignment.Center
                ) {
                    optionContent(option, isSelected)
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
        color = Color.Black,
        optionContent = { option, isSelected ->
            val color = if (isSelected) Color.White else Color.Black
            Text(text = option, color = color)
        },
        modifier = Modifier.fillMaxWidth()
    )
}
