package com.artemchep.keyguard.ui.theme

import androidx.compose.material.LocalContentColor
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontFamily
import com.artemchep.keyguard.common.model.AppColors
import com.artemchep.keyguard.common.model.AppFont
import com.artemchep.keyguard.common.model.AppTheme
import com.artemchep.keyguard.common.usecase.GetColors
import com.artemchep.keyguard.common.usecase.GetFont
import com.artemchep.keyguard.common.usecase.GetTheme
import com.artemchep.keyguard.common.usecase.GetThemeUseAmoledDark
import com.artemchep.keyguard.platform.CurrentPlatform
import com.artemchep.keyguard.platform.theme.hasDarkThemeEnabled
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import kotlin.Boolean
import kotlinx.coroutines.flow.combine
import org.jetbrains.compose.resources.Font
import org.koin.compose.koinInject
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.ui.surface.LocalSurfaceColor
import top.yukonga.miuix.kmp.basic.Scaffold
import androidx.compose.foundation.LocalIndication

val monoFontFamily: FontFamily
    @Composable
    get() = robotoMonoFontFamily

val sansFontFamily: FontFamily
    @Composable
    get() = robotoSansFontFamily

val robotoMonoFontFamily: FontFamily
    @Composable
    get() = FontFamily(Font(Res.font.RobotoMono))

val robotoSansFontFamily: FontFamily
    @Composable
    get() {
        val getFont = koinInject<GetFont>()
        val font = remember(getFont) {
            getFont()
        }.collectAsState(null)
        val res = when (font.value) {
            AppFont.ROBOTO -> Res.font.Roboto_Regular
            AppFont.NOTO -> Res.font.NotoSans_Regular
            AppFont.ATKINSON_HYPERLEGIBLE -> Res.font.AtkinsonHyperlegible_Regular
            null -> Res.font.Roboto_Regular
        }
        return FontFamily(Font(res))
    }

@Composable
fun KeyguardTheme(
    content: @Composable () -> Unit,
) {
    val theme = rememberThemeConfigState()
    val dark = rememberThemeDarkState(
        config = theme.value,
    )
    KeyguardTheme(
        config = theme.value,
        dark = dark.value,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun KeyguardTheme(
    config: ThemeConfig,
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    // Miuix owns the palette. The remaining Material 3 components read a color
    // scheme derived from it, so both stacks stay visually identical.
    MiuixThemeBridge(
        config = config,
        dark = dark,
    ) {
        val colors = MiuixTheme.colorScheme
        // Not remembered: the Miuix scheme is a stable object with snapshot
        // state roles, so the projection has to be re-read on every change.
        val scheme = colors.toMaterialColorScheme()
        // Material provides its own ripple inside MaterialTheme, which would
        // replace the Miuix press effect for every Miuix component.
        val miuixIndication = LocalIndication.current

        val getFont = koinInject<GetFont>()
        val font = remember(getFont) {
            getFont()
        }.collectAsState(null)

        val typography = if (font.value == null) {
            Typography()
        } else {
            val defaultTypography = Typography()
            Typography(
                displayLarge = defaultTypography.displayLarge.copy(fontFamily = sansFontFamily),
                displayMedium = defaultTypography.displayMedium.copy(fontFamily = sansFontFamily),
                displaySmall = defaultTypography.displaySmall.copy(fontFamily = sansFontFamily),

                headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = sansFontFamily),
                headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = sansFontFamily),
                headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = sansFontFamily),

                titleLarge = defaultTypography.titleLarge.copy(fontFamily = sansFontFamily),
                titleMedium = defaultTypography.titleMedium.copy(fontFamily = sansFontFamily),
                titleSmall = defaultTypography.titleSmall.copy(fontFamily = sansFontFamily),

                bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = sansFontFamily),
                bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = sansFontFamily),
                bodySmall = defaultTypography.bodySmall.copy(fontFamily = sansFontFamily),

                labelLarge = defaultTypography.labelLarge.copy(fontFamily = sansFontFamily),
                labelMedium = defaultTypography.labelMedium.copy(fontFamily = sansFontFamily),
                labelSmall = defaultTypography.labelSmall.copy(fontFamily = sansFontFamily),
            )
        }
        MaterialTheme(
            colorScheme = scheme,
            typography = typography,
        ) {
            SystemUiThemeEffect()

            androidx.compose.material.MaterialTheme(
                colors = (if (colors.isDark) darkColors() else lightColors())
                    .copy(
                        primary = colors.primary,
                        onPrimary = colors.onPrimary,
                        secondary = colors.secondary,
                        onSecondary = colors.onSecondary,
                    ),
            ) {
                // The root scaffold paints the app background and, more
                // importantly, hosts every Miuix overlay (dropdowns, dialogs).
                // Overlays are registered into the outermost scaffold, so one
                // root host makes them render from any screen.
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { _ ->
                    CompositionLocalProvider(
                        LocalIndication provides miuixIndication,
                        androidx.compose.material3.LocalContentColor provides colors.onSurface,
                        LocalSurfaceColor provides colors.surface,
                        content = content,
                    )
                }
            }
        }
    }
}

data class ThemeConfig(
    val colors: AppColors?,
    val mode: AppTheme?,
    val useAmoledBlack: Boolean,
)

@Composable
fun rememberThemeConfigState(): State<ThemeConfig> {
    val getTheme = koinInject<GetTheme>()
    val getThemeUseAmoledDark = koinInject<GetThemeUseAmoledDark>()
    val getColors = koinInject<GetColors>()
    return remember(
        getTheme,
        getThemeUseAmoledDark,
        getColors,
    ) {
        val themeColorsFlow = getColors()
        val themeModeFlow = getTheme()
        val themeModeAmoledBlackFlow = getThemeUseAmoledDark()
        combine(
            themeColorsFlow,
            themeModeFlow,
            themeModeAmoledBlackFlow,
        ) { colors, mode, modeAmoledBlack ->
            ThemeConfig(
                colors = colors,
                mode = mode,
                useAmoledBlack = modeAmoledBlack,
            )
        }
    }.collectAsState(
        // use sane defaults
        initial = ThemeConfig(
            colors = null,
            mode = null,
            useAmoledBlack = false,
        )
    )
}

@Composable
fun rememberThemeDarkState(
    config: ThemeConfig,
): State<Boolean> {
    val dark = when (config.mode) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        null -> CurrentPlatform.hasDarkThemeEnabled()
    }
    return rememberUpdatedState(dark)
}

@Composable
 ?: run {
    if (isDarkColorScheme) {
        appDynamicDarkColorScheme()
    } else {
        appDynamicLightColorScheme()
    }
}

@Composable
expect fun appDynamicDarkColorScheme(): ColorScheme

@Composable
expect fun appDynamicLightColorScheme(): ColorScheme

@Composable
expect fun SystemUiThemeEffect()

fun plainDarkColorScheme() = darkColorScheme(
    surfaceContainer = Color(red = 32, green = 33, blue = 33),
    surfaceContainerHigh = Color(red = 43, green = 43, blue = 44),
    surfaceContainerHighest = Color(red = 54, green = 54, blue = 54),
    surfaceContainerLow = Color(red = 27, green = 27, blue = 27),
    surfaceContainerLowest = Color(red = 20, green = 22, blue = 22),
    surfaceTint = Color(red = 250, green = 250, blue = 250),
    surface = Color(red = 20, green = 22, blue = 22),
    background = Color(red = 20, green = 22, blue = 22),
)

fun plainLightColorScheme() = lightColorScheme(
    surfaceContainer = Color(red = 245, green = 245, blue = 245),
    surfaceContainerHigh = Color(red = 238, green = 240, blue = 240),
    surfaceContainerHighest = Color(red = 222, green = 224, blue = 225),
    surfaceContainerLow = Color(red = 250, green = 250, blue = 250),
    surfaceContainerLowest = Color(red = 255, green = 255, blue = 255),
    surfaceTint = Color(red = 27, green = 27, blue = 27),
    surface = Color(red = 255, green = 255, blue = 255),
    background = Color(red = 255, green = 255, blue = 255),
)
