package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
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
    BasicComponent(
        modifier = modifier
            .background(
                MiuixTheme.colorScheme.errorContainer
                    .combineAlpha(DisabledEmphasisAlpha),
            ),
        title = stringResource(Res.string.reused_password),
        titleColor = BasicComponentDefaults.titleColor(
            color = MiuixTheme.colorScheme.onErrorContainer,
        ),
        startAction = {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = contentColor,
            )
        },
        endActions = {
            ChevronIcon()
        },
        onClick = item.onClick,
    )
}
