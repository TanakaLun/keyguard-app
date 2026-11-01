package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.TableRowItem
import com.artemchep.keyguard.ui.util.HorizontalDivider
import com.artemchep.keyguard.ui.theme.titleSmall
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun VaultViewTableItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Table,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    Box {
        BasicComponent(
            modifier = modifier,
            onClick = if (item.dropdown.isNotEmpty()) {
                // lambda
                {
                    dropdownExpanded = true
                }
            } else {
                null
            },
            content = {
                if (item.title != null || item.value != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text = item.title.orEmpty(),
                            style = MiuixTheme.textStyles.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            text = item.value.orEmpty(),
                            style = MiuixTheme.textStyles.titleSmall,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(12.dp),
                    )
                }

                item.rows.forEachIndexed { index, row ->
                    TableRowItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = row.title,
                        text = row.value,
                    )
                    if (index < item.rows.lastIndex) {
                        HorizontalDivider()
                    }
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
