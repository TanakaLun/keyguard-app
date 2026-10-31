package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.model.VaultViewItem
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
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
    BasicComponent(
        modifier = modifier
            .background(
                MiuixTheme.colorScheme.infoContainer
                    .combineAlpha(DisabledEmphasisAlpha),
            ),
        title = stringResource(Res.string.passkey_available),
        titleColor = BasicComponentDefaults.titleColor(
            color = MiuixTheme.colorScheme.onInfoContainer,
        ),
        startAction = {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = contentColor,
            )
        },
        onClick = item.onClick,
    )
}
