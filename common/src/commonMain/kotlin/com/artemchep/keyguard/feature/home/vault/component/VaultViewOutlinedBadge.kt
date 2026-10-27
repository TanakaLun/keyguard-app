package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.ui.util.DividerColor
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.KgShapes

/**
 * A short outlined label shown at the end of an item's
 * main text, for example the URI match type.
 */
@Composable
fun VaultViewOutlinedBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .widthIn(max = 128.dp)
            .border(
                Dp.Hairline,
                DividerColor,
                KgShapes.small,
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        text = text,
        style = MiuixTheme.textStyles.labelSmall,
        textAlign = TextAlign.End,
        maxLines = 2,
    )
}
