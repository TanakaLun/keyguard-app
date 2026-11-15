package com.artemchep.keyguard.feature.home.settings.shortcuts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.GlobalShortcutAction
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.service.shortcut.GlobalShortcutError
import com.artemchep.keyguard.common.service.shortcut.GlobalShortcutStatus
import com.artemchep.keyguard.feature.home.settings.LocalSettingPaneComponents
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.platform.CurrentPlatform
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.ui.KeyguardLoadingIndicator
import com.artemchep.keyguard.ui.ScaffoldColumn
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun KeyboardShortcutsSettingsScreen() {
    val state = produceKeyboardShortcutsSettingsState()
    KeyboardShortcutsSettingsContent(state)
}

@Composable
private fun KeyboardShortcutsSettingsContent(state: Loadable<KeyboardShortcutsSettingsState>) {
    val scrollBehavior = ToolbarBehavior.behavior()
    ScaffoldColumn(
        modifier =
            Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = stringResource(Res.string.keyboard_shortcuts_title),
                navigationIcon = { NavigationIcon() },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        if (state is Loadable.Ok) {
            state.value.items.forEach { item ->
                KeyboardShortcutItem(item, state.value)
            }
        } else {
            KeyguardLoadingIndicator(
                modifier =
                    Modifier
                        .padding(Dimens.contentPadding),
            )
        }
    }
}

@Composable
private fun KeyboardShortcutItem(
    item: KeyboardShortcutsSettingsState.Item,
    state: KeyboardShortcutsSettingsState,
) {
    val title = shortcutActionTitle(item.action)
    val bindingLabel = stringResource(Res.string.keyboard_shortcuts_binding_label, title)
    val binding = item.config.resolve(item.action, CurrentPlatform)
    val bindingText = remember(binding) { binding.displayText() }
    LocalSettingPaneComponents.current.KgSwitch(
        title = { Text(title) },
        text = { KeyboardShortcutStatus(item) },
        checked = item.config.enabled,
        onCheckedChange = item.onEnabledChange.takeUnless { state.busy },
        footer = {
            Row(
                modifier =
                    Modifier.padding(
                        bottom = Dimens.verticalPadding,
                        start = Dimens.horizontalPadding,
                        end = Dimens.horizontalPadding,
                    ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    onClick = item.onEdit,
                    enabled = !state.busy,
                    colors = ButtonDefaults.buttonColors(),
                    modifier =
                        Modifier.semantics {
                            contentDescription = bindingLabel
                            stateDescription = bindingText
                        },
                ) {
                    Text(bindingText)
                }
                TextButton(
                    text = stringResource(Res.string.reset),
                    onClick = item.onReset,
                    enabled = !state.busy && item.config.binding != null,
                )
            }
        },
    )
}

@Composable
private fun KeyboardShortcutStatus(item: KeyboardShortcutsSettingsState.Item) {
    val status =
        when (item.status) {
            GlobalShortcutStatus.Active -> Res.string.keyboard_shortcuts_status_active
            GlobalShortcutStatus.Disabled -> Res.string.disabled
            GlobalShortcutStatus.Recording -> Res.string.keyboard_shortcuts_status_recording
            is GlobalShortcutStatus.Failed -> null
        }
    val failure = item.error ?: (item.status as? GlobalShortcutStatus.Failed)?.error
    Column {
        status?.let { Text(stringResource(it)) }
        failure?.let { Text(shortcutErrorText(it), color = MiuixTheme.colorScheme.error) }
    }
}

@Composable
internal fun shortcutActionTitle(action: GlobalShortcutAction): String =
    stringResource(
        when (action) {
            GlobalShortcutAction.QuickSearch -> Res.string.keyboard_shortcuts_action_quick_search
            GlobalShortcutAction.LockVault -> Res.string.vault_action_lock_vault_title
        },
    )

@Composable
internal fun shortcutErrorText(error: GlobalShortcutError): String =
    when (error) {
        is GlobalShortcutError.Duplicate -> {
            val title = shortcutActionTitle(error.action)
            stringResource(Res.string.keyboard_shortcuts_error_duplicate, title)
        }

        GlobalShortcutError.Invalid -> stringResource(Res.string.keyboard_shortcuts_error_invalid)

        GlobalShortcutError.UnsupportedPlatform ->
            stringResource(Res.string.keyboard_shortcuts_error_unsupported_platform)

        GlobalShortcutError.UnsupportedSession ->
            stringResource(Res.string.keyboard_shortcuts_error_unsupported_session)

        GlobalShortcutError.Unavailable -> stringResource(Res.string.keyboard_shortcuts_error_unavailable)

        GlobalShortcutError.Registration -> stringResource(Res.string.keyboard_shortcuts_error_registration)

        GlobalShortcutError.Persistence -> stringResource(Res.string.keyboard_shortcuts_error_persistence)

        GlobalShortcutError.PersistenceRestoration ->
            stringResource(Res.string.keyboard_shortcuts_error_persistence_restoration)
    }
