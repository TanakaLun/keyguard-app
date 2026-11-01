package com.artemchep.keyguard.feature.gpgagent.tools

import androidx.compose.foundation.layout.ColumnScope
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.bodySmall
import com.artemchep.keyguard.ui.theme.combineAlpha
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun ColumnScope.GpgToolsKeyIdentity(
    key: GpgToolsState.KeyItem,
) {
    Text(
        key.title,
        style = MiuixTheme.textStyles.bodyLarge,
    )
    key.description?.takeIf(String::isNotBlank)?.let { fingerprint ->
        Text(
            fingerprint,
            fontFamily = FontFamily.Monospace,
            style = MiuixTheme.textStyles.bodySmall,
            color = LocalContentColor.current.combineAlpha(MediumEmphasisAlpha),
        )
    }
}
