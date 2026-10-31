package com.artemchep.keyguard.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * `true` while the rendered screen is the root of its navigation stack.
 *
 * Follows the Miuix example: the navigation bar belongs to the home shell and is
 * not shown by the screens pushed on top of a tab root.
 */
val LocalIsStackRootScreen = compositionLocalOf {
    true
}

/**
 * The state the home shell reads to decide whether the navigation bar belongs to
 * the currently visible screen. The stack root screen reports into it.
 */
val LocalIsStackRootScreenSink = staticCompositionLocalOf<MutableState<Boolean>> {
    mutableStateOf(true)
}
