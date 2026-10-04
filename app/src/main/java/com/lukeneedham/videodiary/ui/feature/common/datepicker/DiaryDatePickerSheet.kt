package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AppSurface
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDatePickerSheet(
    initialFocusedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onJumpToToday: (() -> Unit)? = null,
    viewModel: DiaryDatePickerViewModel = koinInject(),
) {
    DisposableEffect(viewModel) {
        onDispose {
            viewModel.dispose()
        }
    }

    var topBarMonthName by remember { mutableStateOf("") }
    var topBarYear by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    /** Animates the sheet away, then dismisses it. */
    fun hideThen(action: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            action()
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppSurface,
        contentColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        dragHandle = { DragHandle() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { hideThen {} }
                    .padding(8.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = topBarMonthName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = topBarYear,
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (onJumpToToday != null) {
                Icon(
                    painter = painterResource(R.drawable.calendar_today),
                    contentDescription = "Jump to today",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { hideThen(onJumpToToday) }
                        .padding(8.dp)
                )
            }
        }
        val videoAspectRatio = viewModel.videoAspectRatio
        if (videoAspectRatio != null) {
            DiaryDatePicker(
                initialFocusedDate = initialFocusedDate,
                months = viewModel.months,
                videoAspectRatio = videoAspectRatio,
                onDateSelected = { hideThen { onDateSelected(it) } },
                onVisibleMonthChanged = { monthName, year ->
                    topBarMonthName = monthName
                    topBarYear = year
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
private fun DragHandle() {
    Spacer(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .size(width = 36.dp, height = 4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.3f)),
    )
}
