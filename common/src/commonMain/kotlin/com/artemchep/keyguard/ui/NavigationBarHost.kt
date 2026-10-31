package com.artemchep.keyguard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * `true` while the rendered screen is the root of its navigation stack.
 *
 * Follows the Miuix example: the navigation bar belongs to the tab root screen
 * and is not rendered by the screens pushed on top of it.
 */
val LocalIsStackRootScreen = compositionLocalOf {
    true
}

/**
 * The home navigation bar, provided by the home screen for the stack roots.
 */
val LocalNavigationBarContent = staticCompositionLocalOf<(@Composable () -> Unit)?> {
    null
}
