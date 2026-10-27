package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.theme.onWarningContainer
import com.artemchep.keyguard.ui.theme.warning
import com.artemchep.keyguard.ui.theme.warningContainer
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.titleSmall

@Composable
fun VaultViewInactiveTotpItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.InactiveTotp,
) {
    val contentColor = MiuixTheme.colorScheme.warning
    val backgroundColor = MiuixTheme.colorScheme.warningContainer
        .combineAlpha(DisabledEmphasisAlpha)
    FlatDropdownSimpleExpressive(
        modifier = modifier,
        shapeState = item.shapeState,
        backgroundColor = backgroundColor,
        contentColor = MiuixTheme.colorScheme.onWarningContainer,
        leading = {
            Icon(
                Icons.Outlined.Warning,
                contentDescription = null,
                tint = contentColor,
            )
        },
        content = {
            FlatItemTextContent(
                title = {
                    Text(
                        text = stringResource(Res.string.twofa_available),
                        style = MiuixTheme.textStyles.titleSmall,
                    )
                },
            )
        },
        trailing = if (item.chevron) {
            // composable
            {
                ChevronIcon()
            }
        } else {
            null
        },
        onClick = item.onClick,
    )
}
