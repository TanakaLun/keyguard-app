package com.artemchep.keyguard.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.artemchep.keyguard.common.model.AppTheme
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.TextStyles
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.kmp.theme.darkColorScheme

/**
 * Phase 0/5 bridge: hosts the Miuix theme tree next to the existing Material 3
 * roots. Miuix components read [MiuixTheme.colorScheme] / [MiuixTheme.textStyles]
 * from this subtree, while the Material 3 stack still drives all legacy UI
 * until the wrappers are migrated (see docs/miuix-migration-plan.md).
 *
 * The controller is derived from the same user settings that feed the Material
 * 3 scheme, so the two stacks stay in sync while they coexist: system mode and
 * key color drive the Miuix scheme, AMOLED black overrides the dark palette,
 * and the user-selected font family is applied to every Miuix text style.
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
    val useAmoledBlack = config.useAmoledBlack && dark
    val darkColors = remember(useAmoledBlack) {
        if (useAmoledBlack) amoledDarkColorScheme() else darkColorScheme()
    }
    val textStyles = remember(sansFontFamily) {
        fontTextStyles(font = sansFontFamily)
    }
    val controller = remember(colorSchemeMode, keyColor, dark, darkColors) {
        ThemeController(
            colorSchemeMode = colorSchemeMode,
            darkColors = darkColors,
            keyColor = keyColor,
            isDark = if (colorSchemeMode == ColorSchemeMode.System) dark else null,
        )
    }
    MiuixTheme(
        controller = controller,
        textStyles = textStyles,
        content = content,
    )
}

/** AMOLED-friendly dark palette: push every surface to pure black. */
@Composable
private fun amoledDarkColorScheme(): top.yukonga.miuix.kmp.theme.Colors {
    val scheme = darkColorScheme()
    scheme.apply {
        background = Color.Black
        surface = Color.Black
        surfaceVariant = Color.Black
        surfaceContainer = Color.Black
        surfaceContainerHigh = Color.Black
        surfaceContainerHighest = Color.Black
    }
    return scheme
}

/** Applies the user-selected sans font family to every Miuix text style. */
@Composable
private fun fontTextStyles(
    font: androidx.compose.ui.text.font.FontFamily,
): TextStyles {
    val base = MiuixTheme.textStyles
    return base.copy(
        main = base.main.copy(fontFamily = font),
        paragraph = base.paragraph.copy(fontFamily = font),
        body1 = base.body1.copy(fontFamily = font),
        body2 = base.body2.copy(fontFamily = font),
        button = base.button.copy(fontFamily = font),
        footnote1 = base.footnote1.copy(fontFamily = font),
        footnote2 = base.footnote2.copy(fontFamily = font),
        headline1 = base.headline1.copy(fontFamily = font),
        headline2 = base.headline2.copy(fontFamily = font),
        subtitle = base.subtitle.copy(fontFamily = font),
        title1 = base.title1.copy(fontFamily = font),
        title2 = base.title2.copy(fontFamily = font),
        title3 = base.title3.copy(fontFamily = font),
        title4 = base.title4.copy(fontFamily = font),
    )
}