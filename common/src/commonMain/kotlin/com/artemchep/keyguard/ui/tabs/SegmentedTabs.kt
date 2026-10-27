package com.artemchep.keyguard.ui.tabs

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupMenuState
import androidx.compose.material3.ButtonGroupScope
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.artemchep.keyguard.feature.localization.textResource
import kotlinx.collections.immutable.ImmutableList
import top.yukonga.miuix.kmp.basic.TabRow

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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

@Composable
private fun SegmentedOverflowIndicator(menuState: ButtonGroupMenuState) {
    FilledIconButton(
        onClick = {
            if (menuState.isShowing) {
                menuState.dismiss()
            } else {
                menuState.show()
            }
        },
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun <T : TabItem> ButtonGroupScope.SegmentedToggleButton(
    tab: T,
    checked: Boolean,
    interactionSource: MutableInteractionSource,
    weight: Float,
    enabled: Boolean,
    onClick: (T) -> Unit,
) {
    ToggleButton(
        modifier = Modifier
            .animateWidth(interactionSource)
            .then(
                if (weight.isNaN()) {
                    Modifier
                } else Modifier.weight(weight),
            ),
        checked = checked,
        enabled = enabled,
        onCheckedChange = { onClick(tab) },
        interactionSource = interactionSource,
    ) {
        SegmentedTabTitle(tab)
    }
}

@Composable
private fun <T : TabItem> SegmentedMenuItem(
    tab: T,
    menuState: ButtonGroupMenuState,
    enabled: Boolean,
    onClick: (T) -> Unit,
) {
    DropdownMenuItem(
        enabled = enabled,
        text = { SegmentedTabTitle(tab) },
        onClick = {
            onClick(tab)
            menuState.dismiss()
        },
    )
}

@Composable
private fun SegmentedTabTitle(tab: TabItem) {
    Text(
        text = textResource(tab.title),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
