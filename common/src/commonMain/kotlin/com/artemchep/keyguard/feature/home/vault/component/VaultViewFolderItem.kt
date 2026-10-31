package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.ui.Breadcrumb
import com.artemchep.keyguard.ui.BreadcrumbTree
import com.artemchep.keyguard.ui.icons.ChevronIcon

@Composable
fun VaultViewFolderItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Folder,
) {
    BasicComponent(
        modifier = modifier,
        startAction = {
            Icon(
                Icons.Outlined.Folder,
                null,
            )
        },
        endActions = {
            ChevronIcon()
        },
        onClick = item.onClick,
    ) {
        VaultViewFolderTree(
            item = item,
        )
    }
}

@Composable
fun VaultViewFolderTree(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Folder,
) {
    val breadcrumbs = remember(item.nodes) {
        item.nodes.map { node ->
            Breadcrumb(
                name = node.name,
                onClick = node.onClick,
            )
        }
    }
    BreadcrumbTree(
        modifier = modifier,
        breadcrumbs = breadcrumbs,
    )
}
