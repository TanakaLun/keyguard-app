package com.artemchep.keyguard.feature.home.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.artemchep.keyguard.feature.navigation.LocalNavigationNodeVisualStack

/**
 * The settings tab.
 *
 * Settings pages are pushed onto the tab stack, so the home shell keeps a single
 * navigation stack per tab.
 */
@Composable
fun SettingsScreen() {
    // Vault screen actually does not add any depth to the
    // navigation stack, it just renders sub-windows.
    val visualStack = LocalNavigationNodeVisualStack.current
        .run {
            removeAt(lastIndex)
        }
    CompositionLocalProvider(
        LocalNavigationNodeVisualStack provides visualStack,
    ) {
        SettingListRoute.Content()
    }
}
