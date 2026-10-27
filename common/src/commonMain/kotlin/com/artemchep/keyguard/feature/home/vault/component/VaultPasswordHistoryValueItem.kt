package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Password
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.artemchep.keyguard.feature.home.vault.model.VaultPasswordHistoryItem
import com.artemchep.keyguard.ui.ExpandedIfNotEmptyForRow
import com.artemchep.keyguard.ui.FlatDropdown
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.colorizePassword
import com.artemchep.keyguard.ui.theme.monoFontFamily
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.state.ToggleableState

@Composable
fun VaultPasswordHistoryValueItem(
    item: VaultPasswordHistoryItem.Value,
) {
    val backgroundColor = when {
        item.selected -> MiuixTheme.colorScheme.primaryContainer
        else -> Color.Unspecified
    }
    FlatDropdownSimpleExpressive(
        backgroundColor = backgroundColor,
        shapeState = item.shapeState,
        leading = {
            val icon = Icons.Outlined.Password
            Icon(icon, null)
        },
        content = {
            FlatItemTextContent(
                title = {
                    Text(
                        text = colorizePassword(item.value, LocalContentColor.current),
                        fontFamily = if (item.monospace) monoFontFamily else null,
                    )
                },
                text = if (!item.date.isNullOrBlank()) {
                    // composable
                    {
                        Text(item.date)
                    }
                } else {
                    null
                },
            )
        },
        dropdown = item.dropdown,
        trailing = {
            val onCopyAction = remember(item.dropdown) {
                item.dropdown
                    .firstNotNullOfOrNull {
                        val action = it as? FlatItemAction
                        action?.takeIf { it.type == FlatItemAction.Type.COPY }
                    }
            }
            if (onCopyAction != null) {
                val onCopy = onCopyAction.onClick
                IconButton(
                    enabled = onCopy != null,
                    onClick = {
                        onCopy?.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null,
                    )
                }
            }
            ExpandedIfNotEmptyForRow(
                item.selected.takeIf { item.selecting },
            ) { selected ->
                Checkbox(
                    state = ToggleableState(selected),
                    onClick = null,
                )
            }
        },
        onClick = item.onClick,
        onLongClick = item.onLongClick,
        enabled = true,
    )
}
