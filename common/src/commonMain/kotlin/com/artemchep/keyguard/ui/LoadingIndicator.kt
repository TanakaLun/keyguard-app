package com.artemchep.keyguard.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator

/**
 * Miuix-backed loading indicator (Phase 1).
 *
 * `contained` maps to an indeterminate linear bar; otherwise an infinite
 * circular indicator is shown.
 */
@Composable
fun KeyguardLoadingIndicator(
    modifier: Modifier = Modifier,
    contained: Boolean = false,
) {
    if (contained) {
        LinearProgressIndicator(
            modifier = modifier,
            progress = null,
        )
    } else {
        InfiniteProgressIndicator(
            modifier = modifier,
        )
    }
}