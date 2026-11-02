package com.artemchep.keyguard.wear.ui

import androidx.compose.material3.ColorScheme as GlobalColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.LocalContentColor
import androidx.wear.compose.material3.LocalTextStyle
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.dynamicColorScheme
import com.artemchep.keyguard.ui.theme.plainDarkColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WearKeyguardTheme(
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val wearColorScheme = remember(context) {
        dynamicColorScheme(context)
    } ?: MaterialTheme.colorScheme
    MaterialTheme(
        colorScheme = wearColorScheme,
    ) {
        val globalColorScheme = remember(wearColorScheme) {
            val baseColorScheme = plainDarkColorScheme()
            baseColorScheme
                .withWearColorScheme(wearColorScheme)
        }
        MaterialExpressiveTheme(
            colorScheme = globalColorScheme,
            content = content,
        )
    }
}

private fun GlobalColorScheme.withWearColorScheme(
    wearColorScheme: ColorScheme,
): GlobalColorScheme = this.copy(
    primary = wearColorScheme.primary,
    primaryContainer = wearColorScheme.primaryContainer,
    onPrimary = wearColorScheme.onPrimary,
    onPrimaryContainer = wearColorScheme.onPrimaryContainer,
    secondary = wearColorScheme.secondary,
    secondaryContainer = wearColorScheme.secondaryContainer,
    onSecondary = wearColorScheme.onSecondary,
    onSecondaryContainer = wearColorScheme.onSecondaryContainer,
    tertiary = wearColorScheme.tertiary,
    tertiaryContainer = wearColorScheme.tertiaryContainer,
    onTertiary = wearColorScheme.onTertiary,
    onTertiaryContainer = wearColorScheme.onTertiaryContainer,
    surfaceContainerLow = wearColorScheme.surfaceContainerLow,
    surfaceContainer = wearColorScheme.surfaceContainer,
    surfaceContainerHigh = wearColorScheme.surfaceContainerHigh,
    onSurface = wearColorScheme.onSurface,
    onSurfaceVariant = wearColorScheme.onSurfaceVariant,
    outline = wearColorScheme.outline,
    outlineVariant = wearColorScheme.outlineVariant,
    background = wearColorScheme.background,
    onBackground = wearColorScheme.onBackground,
    error = wearColorScheme.error,
    errorContainer = wearColorScheme.errorContainer,
    onError = wearColorScheme.onError,
    onErrorContainer = wearColorScheme.onErrorContainer,
)

/**
 * Proxies the wear styles as
 * regular common Material 3 styles.
 */
@Composable
fun ProxyMaterial3Styles(
    content: @Composable () -> Unit,
) {
    val wearContentColor = LocalContentColor.current
    val wearTextStyle = LocalTextStyle.current
    CompositionLocalProvider(
        androidx.compose.material3.LocalContentColor provides wearContentColor,
        androidx.compose.material3.LocalTextStyle provides wearTextStyle,
    ) {
        content()
    }
}
