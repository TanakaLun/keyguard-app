package com.artemchep.keyguard.feature.auth.bitwarden.twofactor

import androidx.compose.foundation.layout.ColumnScope
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import arrow.core.partially1
import com.artemchep.keyguard.feature.fido2.Fido2PromptEffect
import com.artemchep.keyguard.feature.home.vault.component.FlatItemLayoutExpressive
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.res.Res
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.state.ToggleableState

@Composable
internal fun ColumnScope.LoginOtpScreenContentFido2WebAuthn(
    screenScope: LoginOtpScreenScope,
    state: BitwardenLoginTwofaState.Fido2WebAuthn,
) {
    Fido2PromptEffect(state.prompts)
    LoginOtpScreenContentFido2WebAuthnBrowser(screenScope, state)
    FlatItemLayoutExpressive(
        expressive = false,
        leading = {
            Checkbox(
                state = ToggleableState(state.rememberMe.checked),
                enabled = state.rememberMe.onChange != null,
                onClick = null,
            )
        },
        content = {
            Text(
                text = stringResource(Res.string.remember_me),
                style = MiuixTheme.textStyles.bodyMedium,
            )
        },
        onClick = state.rememberMe.onChange?.partially1(!state.rememberMe.checked),
    )
}
