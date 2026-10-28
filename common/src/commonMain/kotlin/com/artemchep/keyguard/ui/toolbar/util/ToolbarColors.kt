package com.artemchep.keyguard.ui.toolbar.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Toolbar colors, following the Miuix page layout: the bar sits on the page
 * surface and does not change its color when the content is scrolled.
 */
object ToolbarColors {
    @Composable
    fun containerColor(): Color = MiuixTheme.colorScheme.surface

    @Composable
    fun scrolledContainerColor(
        containerColor: Color,
    ): Color = containerColor
}
