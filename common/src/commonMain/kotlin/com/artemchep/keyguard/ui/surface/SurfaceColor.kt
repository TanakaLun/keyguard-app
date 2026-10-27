package com.artemchep.keyguard.ui.surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * The surface color of the current screen. Defaults to the Miuix surface, so a
 * screen that does not provide one never falls back to a hardcoded color.
 */
val LocalSurfaceColor = compositionLocalOf {
    MiuixTheme.colorScheme.surface
}

@Composable
inline fun ProvideSurfaceColor(
    color: Color,
    crossinline content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSurfaceColor provides color,
    ) {
        content()
    }
}
