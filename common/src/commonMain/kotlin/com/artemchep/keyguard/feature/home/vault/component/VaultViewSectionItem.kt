package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.ui.theme.LocalExpressive
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
    } else if (LocalExpressive.current) {
        Spacer(
            modifier = Modifier
                .height(24.dp),
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
