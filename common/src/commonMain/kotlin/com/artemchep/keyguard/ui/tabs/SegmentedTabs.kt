package com.artemchep.keyguard.ui.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.localization.textResource
import kotlinx.collections.immutable.ImmutableList
import top.yukonga.miuix.kmp.basic.TabRow

@Composable
fun <T : TabItem> SegmentedButtonGroup(
    tabState: State<T?>,
    tabs: ImmutableList<T>,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    weight: Float = Float.NaN,
    isTabEnabled: (T) -> Boolean = { true },
) {
    val selectedIndex = remember(tabState.value) {
        tabs
            .indexOfFirst { it == tabState.value }
            .coerceIn(0, (tabs.size - 1).coerceAtLeast(0))
    }
    val updatedOnClick by rememberUpdatedState(onClick)
    TabRow(
        modifier = modifier,
        tabs = tabs.map { tab -> textResource(tab.title) },
        selectedTabIndex = selectedIndex,
        onTabSelected = { index ->
            tabs.getOrNull(index)?.let(updatedOnClick)
        },
    )
}