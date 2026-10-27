package com.artemchep.keyguard.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.theme.TextStyles

// ---------------------------------------------------------------------------
// Keyguard semantic roles on top of the Miuix color scheme.
// ---------------------------------------------------------------------------

private const val HUE_ERROR = 0f
private const val HUE_WARNING = 55f
private const val HUE_INFO = 210f
private const val HUE_OK = 120f

private fun buildContentColor(
    hue: Float,
    onDark: Boolean,
): Color {
    val saturation: Float
    val lightness: Float
    if (onDark) {
        saturation = 0.72f
        lightness = 0.86f
    } else {
        saturation = 0.96f
        lightness = 0.80f
    }
    return Color.hsv(
        hue = hue,
        saturation = saturation,
        value = lightness,
    )
}

private fun Colors.buildContainerColor(hue: Float): Color {
    val tint = Color.hsv(hue = hue, saturation = 1f, value = 1f)
    return tint
        .copy(alpha = 0.20f)
        .compositeOver(surfaceVariant)
}

private fun Colors.buildOnContainerColor(hue: Float): Color {
    val onDark = isDark
    val saturation: Float
    val lightness: Float
    if (onDark) {
        saturation = 0.1f
        lightness = 0.9f
    } else {
        saturation = 0.2f
        lightness = 0.2f
    }
    return Color.hsv(hue = hue, saturation = saturation, value = lightness)
}

/** Returns `true` if the color scheme is dark or black, `false` otherwise. */
val Colors.isDark: Boolean
    get() = background.luminance() < 0.5f

val Colors.selectedContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = secondaryContainer

val Colors.onSelectedContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = onSecondaryContainer

val Colors.badgeContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = tertiaryContainer

val Colors.danger: Color
    @Composable
    @ReadOnlyComposable
    get() = error

val Colors.warning: Color
    @Composable
    get() = buildContentColor(HUE_WARNING, onDark = isDark)

val Colors.warningVariant: Color
    @Composable
    get() = warning

val Colors.info: Color
    @Composable
    get() = buildContentColor(HUE_INFO, onDark = isDark)

val Colors.infoVariant: Color
    @Composable
    get() = info

val Colors.ok: Color
    @Composable
    get() = buildContentColor(HUE_OK, onDark = isDark)

val Colors.okVariant: Color
    @Composable
    get() = ok

val Colors.warningContainer: Color
    @Composable
    get() = buildContainerColor(HUE_WARNING)

val Colors.warningContainerVariant: Color
    @Composable
    get() = buildContainerColor(HUE_WARNING)

val Colors.onWarningContainer: Color
    @Composable
    get() = buildOnContainerColor(HUE_WARNING)

val Colors.infoContainer: Color
    @Composable
    get() = buildContainerColor(HUE_INFO)

val Colors.infoContainerVariant: Color
    @Composable
    get() = buildContainerColor(HUE_INFO)

val Colors.onInfoContainer: Color
    @Composable
    get() = buildOnContainerColor(HUE_INFO)

val Colors.okContainer: Color
    @Composable
    get() = buildContainerColor(HUE_OK)

val Colors.okContainerVariant: Color
    @Composable
    get() = buildContainerColor(HUE_OK)

val Colors.onOkContainer: Color
    @Composable
    get() = buildOnContainerColor(HUE_OK)

val Colors.searchHighlightBackgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = tertiaryContainer

val Colors.searchHighlightContentColor: Color
    @Composable
    @ReadOnlyComposable
    get() = onTertiaryContainer

// ---------------------------------------------------------------------------
// Material 3 role aliases that Miuix does not expose directly.
// ---------------------------------------------------------------------------

/** Role alias used by legacy layout code. */
val Colors.onSurfaceVariant: Color
    @Composable
    @ReadOnlyComposable
    get() = onSurfaceSecondary

val Colors.scrim: Color
    @Composable
    @ReadOnlyComposable
    get() = Color.Black.copy(alpha = 0.32f)

val Colors.tertiary: Color
    @Composable
    @ReadOnlyComposable
    get() = primaryVariant

val Colors.onTertiary: Color
    @Composable
    @ReadOnlyComposable
    get() = onTertiaryContainer

val Colors.surfaceTint: Color
    @Composable
    @ReadOnlyComposable
    get() = primary

val Colors.outlineVariant: Color
    @Composable
    @ReadOnlyComposable
    get() = dividerLine

val Colors.surfaceContainerLow: Color
    @Composable
    @ReadOnlyComposable
    get() = surfaceContainer

val Colors.surfaceContainerLowest: Color
    @Composable
    @ReadOnlyComposable
    get() = surface

val Colors.surfaceBright: Color
    @Composable
    @ReadOnlyComposable
    get() = surfaceContainerHighest

val Colors.surfaceDim: Color
    @Composable
    @ReadOnlyComposable
    get() = surfaceContainer

val Colors.inverseSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = onBackground

val Colors.inverseOnSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = background

val Colors.inversePrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = primaryContainer


/**
 * Returns the [Colors.surface] color with the tonal elevation overlay applied.
 */
fun Colors.surfaceColorAtElevation(
    elevation: Dp,
): Color {
    val tint = surfaceColorAtElevationSemi(elevation = elevation)
    return if (tint.isSpecified) {
        tint.compositeOver(surface)
    } else {
        surface
    }
}

/** Returns the tint with the elevation alpha. */
fun Colors.surfaceColorAtElevationSemi(
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return Color.Unspecified
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.copy(alpha = alpha)
}

/** Returns [color] tinted by the tonal elevation overlay. */
fun Colors.colorAtElevation(
    color: Color,
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return color
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.copy(alpha = alpha).compositeOver(color)
}

// ---------------------------------------------------------------------------
// Material 3 typography aliases mapped to Miuix text styles.
// ---------------------------------------------------------------------------

val TextStyles.displayLarge: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = title1

val TextStyles.displayMedium: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = title2

val TextStyles.displaySmall: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = title3

val TextStyles.headlineLarge: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = headline1

val TextStyles.headlineMedium: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = headline1

val TextStyles.headlineSmall: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = headline2

val TextStyles.titleLarge: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = title2

val TextStyles.titleMedium: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = title3

val TextStyles.titleSmall: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = title4

val TextStyles.bodyLarge: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = body1

val TextStyles.bodyMedium: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = body1

val TextStyles.bodySmall: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = body2

val TextStyles.labelLarge: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = button

val TextStyles.labelMedium: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = footnote1

val TextStyles.labelSmall: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = footnote2

val TextStyles.titleLargeEmphasized: TextStyle
    @Composable
    get() = title2.copy(fontWeight = FontWeight.SemiBold)

val TextStyles.titleMediumEmphasized: TextStyle
    @Composable
    get() = title3.copy(fontWeight = FontWeight.SemiBold)

val TextStyles.titleSmallEmphasized: TextStyle
    @Composable
    get() = title4.copy(fontWeight = FontWeight.SemiBold)

// ---------------------------------------------------------------------------
// Shapes: M3 shape roles as Miuix-friendly rounded corners.
// ---------------------------------------------------------------------------

object KgShapes {
    val extraSmall = RoundedCornerShape(4.dp)
    val small = RoundedCornerShape(8.dp)
    val medium = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)
    val extraLarge = RoundedCornerShape(28.dp)
}
