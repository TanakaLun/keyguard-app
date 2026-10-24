package com.artemchep.keyguard.ui.util

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.HorizontalDivider as MiuixHorizontalDivider
import top.yukonga.miuix.kmp.basic.VerticalDivider as MiuixVerticalDivider
import top.yukonga.miuix.kmp.theme.MiuixTheme

val DividerColor
    @Composable
    get() = MiuixTheme.colorScheme.dividerLine

val DividerSize get() = 1.dp

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    transparency: Boolean = true,
) {
    MiuixVerticalDivider(
        modifier = modifier.fillMaxHeight(),
        thickness = DividerSize,
        color = dividerColor(transparency),
    )
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    transparency: Boolean = true,
) {
    MiuixHorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = DividerSize,
        color = dividerColor(transparency),
    )
}

@Composable
private fun dividerColor(
    transparency: Boolean,
): Color {
    val color = DividerColor
    return if (transparency) {
        color
    } else {
        // Opaque dividers sit on the highest container so they remain visible
        // over tinted surfaces.
        color.compositeOver(MiuixTheme.colorScheme.surfaceContainerHighest)
    }
}