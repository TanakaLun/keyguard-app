package com.artemchep.keyguard.feature.gpgkey.expiration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.basic.Checkbox
import androidx.compose.material3.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.service.crypto.GpgKeyExpirationChange
import com.artemchep.keyguard.feature.dialog.Dialog
import com.artemchep.keyguard.feature.navigation.RouteResultTransmitter
import com.artemchep.keyguard.feature.search.filter.component.FilterChipItemComposable
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.FlatItemLayout
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.combineAlpha
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.state.ToggleableState
import com.artemchep.keyguard.ui.theme.bodyMedium
import com.artemchep.keyguard.ui.theme.bodySmall

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GpgKeyExpirationScreen(
    args: GpgKeyExpirationRoute.Args,
    transmitter: RouteResultTransmitter<GpgKeyExpirationChange>,
) {
    val state = gpgKeyExpirationState(
        args = args,
        transmitter = transmitter,
    )
    Dialog(
        title = {
            Text(stringResource(Res.string.gpg_key_expiry_dialog_title))
        },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = Dimens.horizontalPadding),
                    text = stringResource(Res.string.gpg_key_expiry_dialog_message),
                )
                Spacer(modifier = Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.padding(horizontal = Dimens.horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.presets.forEach { preset ->
                        key(preset.key) {
                            FilterChipItemComposable(
                                checked = preset.selected,
                                leading = null,
                                title = preset.title,
                                text = preset.text,
                                onClick = preset.onClick,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.components.forEach { component ->
                        key(component.key) {
                            GpgKeyExpirationComponent(component)
                        }
                    }
                }
                state.validationError?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = Dimens.horizontalPadding),
                        text = error,
                        style = MiuixTheme.textStyles.bodySmall,
                        color = MiuixTheme.colorScheme.error,
                    )
                }
            }
        },
        actions = {
            val updatedOnDeny = rememberUpdatedState(state.onDeny)
            val updatedOnConfirm = rememberUpdatedState(state.onConfirm)
            TextButton(
    text = stringResource(Res.string.cancel),

                enabled = state.onDeny != null,
                onClick = {
                    updatedOnDeny.value?.invoke()
                },
            )
            TextButton(
    text = stringResource(Res.string.ok),

                enabled = state.onConfirm != null,
                onClick = {
                    updatedOnConfirm.value?.invoke()
                },
            )
        },
    )
}

@Composable
private fun GpgKeyExpirationComponent(
    component: GpgKeyExpirationState.Component,
) {
    FlatItemLayout(
        leading = {
            Checkbox(
                state = ToggleableState(component.selected),
                onClick = null,
            )
        },
        content = {
            Column {
                Text(
                    text = component.title,
                    style = MiuixTheme.textStyles.bodyMedium,
                )
                Text(
                    text = component.text,
                    style = MiuixTheme.textStyles.bodySmall,
                    color = LocalContentColor.current.combineAlpha(MediumEmphasisAlpha),
                )
            }
        },
        onClick = component.onToggle,
    )
}
