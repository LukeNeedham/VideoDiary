package com.lukeneedham.videodiary.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.lukeneedham.common.ui.DebugTheme
import com.lukeneedham.common.ui.ProvideDebugSettings

/**
 * [ProvideDebugSettings] with the shared library's debug pages themed to match the app's Material
 * theme. Place it inside the app's theme, since it reads the colours and font from there.
 */
// lint:ignore PRV-7: only provides the debug settings, it draws nothing of its own
@Composable
fun ProvideAppDebugSettings(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colors
    ProvideDebugSettings(
        theme = DebugTheme(
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            outline = colors.onBackground.copy(alpha = 0.4f),
            error = colors.error,
            warning = colors.secondary,
            fontFamily = MaterialTheme.typography.body1.fontFamily ?: FontFamily.Default,
        ),
        content = content,
    )
}
