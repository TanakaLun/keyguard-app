package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import top.yukonga.miuix.kmp.basic.BasicComponent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.cipher_link_unavailable_title
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.icons.IconBox
import org.jetbrains.compose.resources.stringResource

@Composable
fun VaultViewLinkItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Link,
) {
    val presentation = item.presentation
    if (presentation != null) {
        CompactVaultItem(
            modifier = modifier,
            item = presentation,
            trailing = {
                ChevronIcon()
            },
            onClick = item.onClick,
        )
        return
    }

    BasicComponent(
        modifier = modifier,
        title = stringResource(Res.string.cipher_link_unavailable_title),
        startAction = {
            IconBox(main = Icons.Outlined.ErrorOutline)
        },
    )
}
