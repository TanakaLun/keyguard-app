package com.artemchep.keyguard.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import top.yukonga.miuix.kmp.theme.Colors

/**
 * Projects the Miuix palette onto a Material 3 [ColorScheme].
 *
 * Keyguard is a Miuix app: the palette comes from [MiuixThemeBridge] and the
 * Material 3 color scheme only exists for the few components that are still
 * Material (date/time pickers, desktop-only prompts). Deriving it from
 * [Colors] keeps those components on the same palette instead of introducing a
 * second, Material-looking one.
 */
fun Colors.toMaterialColorScheme(): ColorScheme {
    val base = if (isDark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = primary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = primaryVariant,
        onTertiary = onTertiaryContainer,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceSecondary,
        surfaceTint = primary,
        inverseSurface = onBackground,
        inverseOnSurface = background,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = dividerLine,
        surfaceBright = surfaceContainerHighest,
        surfaceDim = surface,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainer,
        surfaceContainerLowest = surface,
    )
}
