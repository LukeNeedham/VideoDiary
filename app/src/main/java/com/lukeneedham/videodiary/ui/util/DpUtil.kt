package com.lukeneedham.videodiary.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

object DpUtil {
    @Composable
    fun fromPxToDp(px: Float) = with(LocalDensity.current) { px.toDp() }
}
