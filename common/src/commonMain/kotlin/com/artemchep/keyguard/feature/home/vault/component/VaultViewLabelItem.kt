package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.combineAlpha
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.labelMedium

@Composable
fun VaultViewLabelItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.Label,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.textHorizontalPadding,
                vertical = 8.dp,
            ),
        horizontalArrangement = item.horizontalArrangement,
    ) {
        val textAlign = if (item.horizontalArrangement == Arrangement.Center) {
            TextAlign.Center
        } else {
            TextAlign.Start
        }
        Text(
            text = item.text,
            textAlign = textAlign,
            style = MiuixTheme.textStyles.labelMedium,
            color =
            if (item.error) {
                MiuixTheme.colorScheme.error
            } else {
                LocalContentColor.current
                    .combineAlpha(MediumEmphasisAlpha)
            },
        )
    }
}
