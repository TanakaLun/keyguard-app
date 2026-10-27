package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.theme.info
import com.artemchep.keyguard.ui.theme.infoContainer
import com.artemchep.keyguard.ui.theme.onInfoContainer
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun VaultViewInactivePasskeyItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.InactivePasskey,
) {
    val contentColor = MiuixTheme.colorScheme.info
    val backgroundColor = MiuixTheme.colorScheme.infoContainer
        .combineAlpha(DisabledEmphasisAlpha)
    FlatItemLayoutExpressive(
        modifier = modifier,
        shapeState = item.shapeState,
        backgroundColor = backgroundColor,
        contentColor = MiuixTheme.colorScheme.onInfoContainer,
        leading = {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = contentColor,
            )
        },
        content = {
            FlatItemTextContent(
                title = {
                    Text(
                        stringResource(Res.string.passkey_available),
                        style = MiuixTheme.textStyles.titleSmall,
                    )
                },
            )
        },
        onClick = item.onClick,
    )
}
