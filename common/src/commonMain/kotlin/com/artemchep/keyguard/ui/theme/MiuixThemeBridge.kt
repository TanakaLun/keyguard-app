package com.artemchep.keyguard.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.artemchep.keyguard.common.model.AppTheme
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

/**
 * Phase 0 bridge: hosts the Miuix theme tree next to the existing Material 3
 * roots. Miuix components read [MiuixTheme.colorScheme] / [MiuixTheme.textStyles]
 * from this subtree, while the Material 3 stack still drives all legacy UI
 * until the wrappers are migrated (see docs/miuix-migration-plan.md).
 *
 * The controller is derived from the same user settings that feed the Material
 * 3 scheme, so the two stacks stay in sync while they coexist.
 */
@Composable
fun MiuixThemeBridge(
    config: ThemeConfig,
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    val colorSchemeMode = when (config.mode) {
        AppTheme.DARK -> ColorSchemeMode.Dark
        AppTheme.LIGHT -> ColorSchemeMode.Light
        null -> ColorSchemeMode.System
    }
    val keyColor = config.colors
        ?.color
        ?.let(::Color)
    val controller = remember(colorSchemeMode, keyColor, dark) {
        ThemeController(
            colorSchemeMode = colorSchemeMode,
            // Phase 5: map the AMOLED palette, palette style, color spec and
            // font text styles here.
            keyColor = keyColor,
            isDark = if (colorSchemeMode == ColorSchemeMode.System) dark else null,
        )
    }
    MiuixTheme(
        controller = controller,
        content = content,
    )
}