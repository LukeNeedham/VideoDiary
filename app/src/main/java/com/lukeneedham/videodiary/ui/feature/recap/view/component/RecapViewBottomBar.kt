package com.lukeneedham.videodiary.ui.feature.recap.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.FlatIconButton
import com.lukeneedham.videodiary.ui.theme.Typography

/**
 * A recap's static bottom bar: a borderless back button, the (wrapping, truly-centered) recap
 * name, and save/share icon buttons - plain, on the same black bar every other video toolbar
 * uses (no surface color/rounded corners of its own).
 */
@Composable
fun RecapViewBottomBar(
    name: String,
    onNameChange: (String) -> Unit,
    isSaved: Boolean,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onToggleSavedClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.width(96.dp),
        ) {
            if (canGoBack) {
                FlatIconButton(
                    iconRes = R.drawable.back,
                    contentDescription = "Back",
                    onClick = onBack,
                    selected = true,
                    size = 44.dp,
                )
            }
        }

        RecapName(
            name = name,
            onNameChange = onNameChange,
            modifier = Modifier.weight(1f),
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(96.dp),
        ) {
            FlatIconButton(
                iconRes = if (isSaved) R.drawable.heart_filled else R.drawable.heart_outline,
                contentDescription = if (isSaved) "Saved" else "Save",
                onClick = onToggleSavedClick,
                selected = true,
                size = 44.dp,
                iconSize = 20.dp,
            )
            FlatIconButton(
                iconRes = R.drawable.share_android,
                contentDescription = "Share",
                onClick = onShareClick,
                selected = true,
                size = 44.dp,
                iconSize = 20.dp,
            )
        }
    }
}

/**
 * The recap name, which turns into a text field in-place when clicked. A pencil icon is shown to
 * the right of the name when not editing, and a tick icon to save the new name when editing.
 */
@Composable
private fun RecapName(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isEditing by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf(name) }
    val focusRequester = remember { FocusRequester() }
    val textStyle = TextStyle(
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = Typography.Size.medium,
    )

    fun save() {
        if (draft.isNotBlank()) {
            onNameChange(draft)
        }
        isEditing = false
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        if (isEditing) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            BasicTextField(
                value = draft,
                onValueChange = { draft = it },
                textStyle = textStyle,
                cursorBrush = SolidColor(AccentHighlight),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { save() }),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .padding(vertical = 4.dp),
            )
            FlatIconButton(
                iconRes = R.drawable.tick,
                contentDescription = "Save name",
                onClick = { save() },
                selected = true,
                size = 40.dp,
                iconSize = 20.dp,
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        draft = name
                        isEditing = true
                    }
                    .padding(vertical = 4.dp),
            ) {
                Text(
                    text = name,
                    style = textStyle,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = "Edit name",
                    colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.6f)),
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
