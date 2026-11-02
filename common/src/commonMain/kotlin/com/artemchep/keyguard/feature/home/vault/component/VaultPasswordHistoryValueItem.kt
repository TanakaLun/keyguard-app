package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Password
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import com.artemchep.keyguard.feature.home.vault.model.VaultPasswordHistoryItem
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.ExpandedIfNotEmptyForRow
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.colorizePassword
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.bodyMedium
import com.artemchep.keyguard.ui.theme.combineAlpha
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
    var dropdownExpanded by remember { mutableStateOf(false) }
    val onLongClick = item.onLongClick
    val onClick: (() -> Unit)? = when {
        item.onClick != null -> item.onClick
        item.dropdown.isNotEmpty() -> {
            // lambda
            {
                dropdownExpanded = true
            }
        }

        else -> null
    }
    Box {
        BasicComponent(
            modifier = Modifier
                .then(
                    if (backgroundColor.isSpecified) {
                        Modifier.background(backgroundColor)
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (onClick != null || onLongClick != null) {
                        Modifier.combinedClickable(
                            onClick = onClick ?: {},
                            onLongClick = onLongClick,
                        )
                    } else {
                        Modifier
                    },
                ),
            startAction = {
                val icon = Icons.Outlined.Password
                Icon(icon, null)
            },
            content = {
                Text(
                    text = colorizePassword(item.value, LocalContentColor.current),
                    fontFamily = if (item.monospace) monoFontFamily else null,
                    style = MiuixTheme.textStyles.bodyLarge,
                )
                if (!item.date.isNullOrBlank()) {
                    Text(
                        text = item.date,
                        style = MiuixTheme.textStyles.bodyMedium,
                        color = LocalContentColor.current
                            .combineAlpha(MediumEmphasisAlpha),
                    )
                }
            },
            endActions = {
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
        )
        KeyguardDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = {
                dropdownExpanded = false
            },
        ) {
            item.dropdown.forEach { action ->
                DropdownMenuItemFlat(
                    action = action,
                )
            }
        }
    }
}
