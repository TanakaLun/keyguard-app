package com.artemchep.keyguard.ui.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.ArrowRight
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * The standard Miuix disclosure arrow, matching [ArrowPreference]s end action.
 */
@Composable
fun ChevronIcon(
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current
    val color = MiuixTheme.colorScheme.onSurfaceVariantActions
    val colorFilter = remember(color) {
        ColorFilter.tint(color)
    }
    Image(
        modifier = modifier
            .size(width = 10.dp, height = 16.dp)
            .graphicsLayer {
                scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f
            },
        imageVector = MiuixIcons.Basic.ArrowRight,
        contentDescription = null,
        colorFilter = colorFilter,
    )
}
