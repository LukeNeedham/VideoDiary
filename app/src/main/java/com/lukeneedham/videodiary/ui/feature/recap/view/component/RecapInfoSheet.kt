package com.lukeneedham.videodiary.ui.feature.recap.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.recap.create.component.RecapThumbnailRow
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * A bottom sheet with the details of a recap: its name (which can be edited), its date range, and
 * the videos in it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapInfoSheet(
    name: String,
    onNameChange: (String) -> Unit,
    startDate: LocalDate,
    endDate: LocalDate,
    thumbnails: List<RecapDayThumbnail>,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SheetDefaults.containerColor,
        contentColor = SheetDefaults.contentColor,
        shape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        dragHandle = null,
        // Handled by SheetLayout, so that the sheet can animate fully out
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        SheetLayout(
            onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
            },
        ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        ) {
            EditableName(name = name, onNameChange = onNameChange)

            val dateFormatter = StandardDateTimeFormatter.dateLong
            Text(
                text = "${startDate.format(dateFormatter)} - ${endDate.format(dateFormatter)}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = Typography.Size.small,
            )

            Text(
                text = "${thumbnails.size} ${if (thumbnails.size == 1) "video" else "videos"}",
                color = Color.White,
                fontSize = Typography.Size.small,
                fontWeight = FontWeight.SemiBold,
            )
            RecapThumbnailRow(
                thumbnails = thumbnails,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        }
    }
}

/**
 * The recap name, with a pencil icon to its right. Clicking the pencil turns the name into a text
 * field (with the cursor at the end of the existing text), and the pencil into a tick to save it.
 */
@Composable
private fun EditableName(
    name: String,
    onNameChange: (String) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf(TextFieldValue(name)) }
    val focusRequester = remember { FocusRequester() }
    val textStyle = TextStyle(
        color = Color.White,
        fontSize = Typography.Size.big,
        fontWeight = FontWeight.Bold,
    )

    fun startEditing() {
        draft = TextFieldValue(text = name, selection = TextRange(name.length))
        isEditing = true
    }

    fun save() {
        if (draft.text.isNotBlank()) {
            onNameChange(draft.text)
        }
        isEditing = false
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isEditing) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            BasicTextField(
                value = draft,
                onValueChange = { draft = it },
                textStyle = textStyle,
                singleLine = true,
                cursorBrush = SolidColor(AccentHighlight),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { save() }),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
            )
        } else {
            Text(
                text = name,
                style = textStyle,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clickable { if (isEditing) save() else startEditing() },
        ) {
            Image(
                painter = painterResource(if (isEditing) R.drawable.tick else R.drawable.edit),
                contentDescription = if (isEditing) "Save name" else "Edit name",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    RecapInfoSheet(
        name = "Spring Trip",
        onNameChange = {},
        startDate = LocalDate.of(2026, 3, 1),
        endDate = LocalDate.of(2026, 5, 31),
        thumbnails = emptyList(),
        onDismiss = {},
    )
}
