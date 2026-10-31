package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.icons.KeyguardCollection

@Composable
fun VaultViewCollectionItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Collection,
) {
    BasicComponent(
        modifier = modifier,
        title = item.title,
        startAction = {
            Icon(
                Icons.Outlined.KeyguardCollection,
                null,
            )
        },
        endActions = {
            ChevronIcon()
        },
        onClick = item.onClick,
    )
}
