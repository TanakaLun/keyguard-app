package com.artemchep.keyguard.feature.agent.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Terminal
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.graphics.isSpecified
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.agent_auto_setup_applied
import com.artemchep.keyguard.res.agent_auto_setup_check_failed
import com.artemchep.keyguard.res.agent_auto_setup_check_retry_action
import com.artemchep.keyguard.res.agent_auto_setup_check_succeeded
import com.artemchep.keyguard.res.agent_auto_setup_checking_status
import com.artemchep.keyguard.res.agent_auto_setup_conflicts_title
import com.artemchep.keyguard.res.agent_auto_setup_diagnostics_title
import com.artemchep.keyguard.res.agent_auto_setup_error_details_title
import com.artemchep.keyguard.res.agent_auto_setup_external_check_text
import com.artemchep.keyguard.res.agent_auto_setup_external_unverified_note
import com.artemchep.keyguard.res.agent_auto_setup_files_to_change_plural
import com.artemchep.keyguard.res.agent_auto_setup_gpg_note
import com.artemchep.keyguard.res.agent_auto_setup_host_label
import com.artemchep.keyguard.res.agent_auto_setup_hosts_all
import com.artemchep.keyguard.res.agent_auto_setup_hosts_one
import com.artemchep.keyguard.res.agent_auto_setup_no_changes
import com.artemchep.keyguard.res.agent_auto_setup_refresh_action
import com.artemchep.keyguard.res.agent_auto_setup_ssh_config_only
import com.artemchep.keyguard.res.agent_auto_setup_ssh_note
import com.artemchep.keyguard.res.agent_auto_setup_termux_run_note
import com.artemchep.keyguard.res.agent_auto_setup_unavailable
import com.artemchep.keyguard.res.agent_auto_setup_undone
import com.artemchep.keyguard.ui.ExpandedIfNotEmpty
import com.artemchep.keyguard.ui.FlatSimpleNote
import com.artemchep.keyguard.ui.FlatTextField
import com.artemchep.keyguard.ui.KeyguardLoadingIndicator
import com.artemchep.keyguard.ui.SimpleNote
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.icons.DropdownIcon
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.wizard.WizardChoice
import com.artemchep.keyguard.ui.wizard.wizardSelectedBackground
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun AgentWizardPage(state: AgentAutomaticSetupState, page: AgentSetupStep) {
    if (!state.data.available) {
        val error = state.data.unavailableError
        if (error != null) {
            AgentWizardError(error)
        } else {
            FlatSimpleNote(type = SimpleNote.Type.INFO, text = stringResource(Res.string.agent_auto_setup_unavailable))
        }
        return
    }
    if (!state.data.setupAllowed && page != AgentSetupStep.RESULT) {
        if (page != AgentSetupStep.REVIEW || state.data.external) {
            FlatSimpleNote(type = SimpleNote.Type.INFO, text = agentSetupEnableMessage(state.data.agent))
        }
        return
    }
    when (page) {
        AgentSetupStep.SETTINGS -> AgentWizardSettings(state)
        AgentSetupStep.REVIEW -> if (state.data.external) {
            AgentWizardExternalRun(state.data)
        } else {
            AgentWizardReview(state)
        }
        AgentSetupStep.RESULT -> AgentWizardResult(state)
    }
}

@Composable
private fun AgentWizardSettings(state: AgentAutomaticSetupState) {
    val data = state.data
    if (data.sshConfig) {
        Column(Modifier.selectableGroup()) {
            AgentHostChoice(Res.string.agent_auto_setup_hosts_all, !data.oneHost, data.editable) {
                state.onHostScopeChange(false)
            }
            AgentHostChoice(Res.string.agent_auto_setup_hosts_one, data.oneHost, data.editable) {
                state.onHostScopeChange(true)
            }
        }
        if (data.oneHost) {
            FlatTextField(
                modifier = Modifier.padding(horizontal = Dimens.fieldHorizontalPadding),
                label = stringResource(Res.string.agent_auto_setup_host_label),
                value = state.host,
                singleLine = true,
            )
        }
    }
    Column {
        data.shells.forEach { shell ->
            val checked = shell in data.selectedShells
            val backgroundColor = wizardSelectedBackground(checked)
            BasicComponent(
                modifier = Modifier
                    .semantics {
                        role = Role.Checkbox
                        toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
                    }
                    .then(
                        if (backgroundColor.isSpecified) {
                            Modifier.background(backgroundColor)
                        } else {
                            Modifier
                        },
                    ),
                title = shell.title,
                startAction = { Icon(Icons.Outlined.Terminal, contentDescription = null) },
                endActions = {
                    Checkbox(
                        state = ToggleableState(checked),
                        enabled = data.editable,
                        onClick = null,
                    )
                },
                onClick = { state.onShellToggle(shell) }.takeIf { data.editable },
            )
        }
    }
}

@Composable
private fun AgentHostChoice(
    title: StringResource,
    checked: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) = WizardChoice(
    title = stringResource(title),
    icon = Icons.Outlined.Language,
    selected = checked,
    enabled = enabled,
    onClick = onClick,
)

@Composable
private fun AgentWizardReview(state: AgentAutomaticSetupState) {
    val data = state.data
    if (data.operation == AgentSetupOperation.REVIEWING) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { KeyguardLoadingIndicator() }
    }
    data.error?.let { AgentWizardError(it) }
    data.plan?.let { plan ->
        AgentWizardSummary(data)
        if (plan.overrides.isNotEmpty()) AgentWizardOverrides(plan.overrides)
        if (plan.conflicts.isNotEmpty()) {
            FlatSimpleNote(
                type = SimpleNote.Type.WARNING,
                title = stringResource(Res.string.agent_auto_setup_conflicts_title),
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.verticalPadding)) {
                        plan.conflicts.forEach { conflict ->
                            Text(agentSetupConflictText(conflict), style = MiuixTheme.textStyles.body2)
                        }
                    }
                },
            )
        }
        // Configure mode already says this in the step description.
        if (plan.undo && plan.edits.isEmpty() && plan.conflicts.isEmpty()) {
            FlatSimpleNote(type = SimpleNote.Type.INFO, text = stringResource(Res.string.agent_auto_setup_no_changes))
        }
        if (plan.edits.isNotEmpty()) {
            AgentSetupBodyLabel(
                pluralStringResource(
                    Res.plurals.agent_auto_setup_files_to_change_plural,
                    plan.edits.size,
                    plan.edits.size,
                ),
            )
            Column {
                plan.edits.forEach { edit ->
                    AgentWizardFile(edit)
                }
            }
        }
        if (!plan.undo) {
            AgentWizardNotes(data.agent)
        }
    }
    if (data.reviewStatus == AgentSetupReviewStatus.READY) {
        TextButton(
            modifier = Modifier.padding(horizontal = Dimens.fieldHorizontalPadding),
            text = stringResource(Res.string.agent_auto_setup_refresh_action),
            onClick = { state.onRefresh?.invoke() },
            enabled = state.onRefresh != null,
        )
    }
}

@Composable
private fun AgentWizardSummary(data: AgentSetupData) {
    if (data.mode == AgentSetupMode.UNDO) return
    val hosts = if (data.oneHost) data.host.trim() else stringResource(Res.string.agent_auto_setup_hosts_all)
    val shells = data.selectedShells.joinToString { it.title }.ifEmpty {
        stringResource(Res.string.agent_auto_setup_ssh_config_only)
    }
    BasicComponent(
        startAction = { Icon(Icons.Outlined.Terminal, contentDescription = null) },
        content = {
            Text(shells, style = MiuixTheme.textStyles.title3)
            if (data.agent == SetupAgent.SSH) {
                Text(hosts, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
            }
        },
        enabled = true,
    )
}

@Composable
private fun AgentWizardNotes(agent: SetupAgent) {
    val color = LocalContentColor.current.combineAlpha(MediumEmphasisAlpha)
    Column(
        modifier = Modifier.padding(horizontal = Dimens.textHorizontalPadding, vertical = Dimens.verticalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.verticalPadding),
    ) {
        Icon(Icons.Outlined.Info, contentDescription = null, tint = color)
        val note = when (agent) {
            SetupAgent.SSH -> Res.string.agent_auto_setup_ssh_note
            SetupAgent.GPG -> Res.string.agent_auto_setup_gpg_note
        }
        Text(stringResource(note), style = MiuixTheme.textStyles.body2, color = color)
    }
}

@Composable
private fun AgentWizardError(error: AgentSetupError) {
    FlatSimpleNote(type = SimpleNote.Type.ERROR, text = agentSetupErrorText(error))
    error.detail?.let { detail ->
        AgentWizardDetails(title = stringResource(Res.string.agent_auto_setup_error_details_title)) {
            AgentSetupCodeBlock(detail)
        }
    }
}

@Composable
private fun AgentWizardDetails(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    var expanded by rememberSaveable(title) { mutableStateOf(false) }
    Column {
        BasicComponent(
            title = title,
            startAction = { Icon(Icons.Outlined.Description, contentDescription = null) },
            endActions = { DropdownIcon(expanded = expanded) },
            onClick = { expanded = !expanded },
        )
        ExpandedIfNotEmpty(Unit.takeIf { expanded }) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.verticalPadding), content = content)
        }
    }
}

@Composable
private fun AgentWizardExternalRun(data: AgentSetupData) {
    if (data.hostCli != null) {
        AgentSetupCodeBlock(hostCliCommand(data, data.hostCli))
        return
    }
    if (data.mode == AgentSetupMode.CONFIGURE) {
        AgentSetupParagraph(stringResource(Res.string.agent_auto_setup_termux_run_note))
    }
    AgentSetupCodeBlock(termuxSetupCommand(data))
}

@Composable
private fun AgentWizardResult(state: AgentAutomaticSetupState) {
    val data = state.data
    if (data.external) {
        FlatSimpleNote(
            type = SimpleNote.Type.INFO,
            text = stringResource(Res.string.agent_auto_setup_external_unverified_note),
        )
        if (data.mode == AgentSetupMode.CONFIGURE) {
            AgentSetupParagraph(stringResource(Res.string.agent_auto_setup_external_check_text))
            AgentSetupCodeBlock(externalCheckCommand(data.agent, data.hostCli?.endpoint))
        }
        return
    }
    Column {
        AgentSetupStatusRow(
            status = AgentSetupStatus.OK,
            text = stringResource(when {
                data.mode == AgentSetupMode.UNDO -> Res.string.agent_auto_setup_undone
                data.applied -> Res.string.agent_auto_setup_applied
                else -> Res.string.agent_auto_setup_no_changes
            }),
        )
        if (data.operation == AgentSetupOperation.VERIFYING) {
            AgentSetupStatusRow(AgentSetupStatus.PENDING, stringResource(Res.string.agent_auto_setup_checking_status))
        }
        if (data.verified == true) {
            AgentSetupStatusRow(AgentSetupStatus.OK, stringResource(Res.string.agent_auto_setup_check_succeeded))
        }
        if (data.verified == false) {
            AgentSetupStatusRow(AgentSetupStatus.WARNING, stringResource(Res.string.agent_auto_setup_check_failed))
        }
    }
    if (data.verified == false) {
        TextButton(
            modifier = Modifier.padding(horizontal = Dimens.fieldHorizontalPadding),
            text = stringResource(Res.string.agent_auto_setup_check_retry_action),
            onClick = { state.onRetry?.invoke() },
            enabled = state.onRetry != null,
        )
    }
    (data.error?.detail ?: data.diagnostic)?.let { diagnostic ->
        AgentWizardDetails(title = stringResource(Res.string.agent_auto_setup_diagnostics_title)) {
            AgentSetupCodeBlock(diagnostic)
        }
    }
}
