package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.Box
import top.yukonga.miuix.kmp.basic.BasicComponent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.SwitchExpressive

@Composable
fun VaultViewSwitchItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Switch,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    Box {
        BasicComponent(
            modifier = modifier,
            summary = item.title,
            endActions = {
                SwitchExpressive(
                    checked = item.value,
                    onCheckedChange = null,
                )
            },
            onClick = if (item.dropdown.isNotEmpty()) {
                // lambda
                {
                    dropdownExpanded = true
                }
            } else {
                null
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
