package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun VaultViewActionItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Action,
) {
    BasicComponent(
        modifier = modifier,
        startAction = if (item.leading != null) {
            // composable
            {
                Row {
                    item.leading?.invoke(this)
                }
            }
        } else {
            null
        },
        endActions = item.trailing,
        onClick = item.onClick,
    ) {
        Text(
            text = item.title,
            fontSize = MiuixTheme.textStyles.headline1.fontSize,
            fontWeight = FontWeight.Medium,
        )
        item.text?.let { text ->
            Text(
                text = text,
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            )
        }
        item.badge?.let { badge ->
            Spacer(
                modifier = Modifier
                    .height(4.dp),
            )
            com.artemchep.keyguard.ui.Ah(
                score = badge.score,
                text = badge.text,
            )
        }
    }
}
