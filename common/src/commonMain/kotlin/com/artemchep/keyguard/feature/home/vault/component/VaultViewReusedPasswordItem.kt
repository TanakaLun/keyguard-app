package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.material3.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
import com.artemchep.keyguard.ui.FlatItemLayout
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.theme.combineAlpha
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.titleSmall

@Composable
fun VaultViewReusedPasswordItem(
    modifier: Modifier = Modifier,
    item: VaultViewItem.ReusedPassword,
) {
    val contentColor = MiuixTheme.colorScheme.error
    val backgroundColor = MiuixTheme.colorScheme.errorContainer
        .combineAlpha(DisabledEmphasisAlpha)
    FlatItemLayoutExpressive(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shapeState = item.shapeState,
        leading = {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = contentColor,
            )
        },
        content = {
            CompositionLocalProvider(
                LocalContentColor provides MiuixTheme.colorScheme.onErrorContainer,
            ) {
                FlatItemTextContent(
                    title = {
                        Text(
                            stringResource(Res.string.reused_password),
                            style = MiuixTheme.textStyles.titleSmall,
                        )
                    },
                )
            }
        },
        trailing = {
            ChevronIcon()
        },
        onClick = item.onClick,
    )
}
