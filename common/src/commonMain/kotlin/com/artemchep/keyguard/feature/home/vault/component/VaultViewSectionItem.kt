package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.SmallTitle

@Composable
fun VaultViewSectionItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Section,
) {
    val text = item.text
    if (text != null) {
        SmallTitle(
            modifier = modifier,
            text = text,
        )
    } else {
        HorizontalDivider(
            modifier = modifier
                .padding(
                    vertical = 8.dp,
                ),
        )
    }
}
