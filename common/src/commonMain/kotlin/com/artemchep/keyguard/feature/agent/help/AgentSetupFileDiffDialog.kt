package com.artemchep.keyguard.feature.agent.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Remove
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.artemchep.keyguard.common.service.clipboard.ClipboardService
import com.artemchep.keyguard.feature.dialog.DialogContent
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.agent_auto_setup_file_create_label
import com.artemchep.keyguard.res.agent_auto_setup_file_remove_label
import com.artemchep.keyguard.res.agent_auto_setup_file_update_label
import com.artemchep.keyguard.res.close
import com.artemchep.keyguard.res.text_diff_copy
import com.artemchep.keyguard.res.text_diff_title
import com.artemchep.keyguard.ui.DialogPopup
import com.artemchep.keyguard.ui.diff.PreparedTextDiff
import com.artemchep.keyguard.ui.diff.TextDiff
import com.artemchep.keyguard.ui.diff.rememberTextDiff
import com.artemchep.keyguard.ui.icons.IconBox
import com.artemchep.keyguard.ui.screenMaxWidth
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.wizard.WizardPreview
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun AgentWizardFile(edit: AgentSetupEdit) {
    var showDiff by remember(edit) { mutableStateOf(false) }
    val action = edit.action
    val actionText = stringResource(action.text)
    BasicComponent(
        modifier = Modifier.semantics { stateDescription = actionText },
        title = edit.path,
        startAction = {
            IconBox(main = Icons.Outlined.Description, secondary = action.icon)
        },
        endActions = { Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null) },
        onClick = { showDiff = true },
    )
    if (showDiff) AgentSetupFileDiffDialog(edit, onDismiss = { showDiff = false })
}

private enum class AgentSetupFileAction(val text: StringResource, val icon: ImageVector) {
    CREATE(Res.string.agent_auto_setup_file_create_label, Icons.Outlined.Add),
    REMOVE(Res.string.agent_auto_setup_file_remove_label, Icons.Outlined.Remove),
    UPDATE(Res.string.agent_auto_setup_file_update_label, Icons.Outlined.Edit),
}

private val AgentSetupEdit.action get() = when {
    before == null -> AgentSetupFileAction.CREATE
    after == null -> AgentSetupFileAction.REMOVE
    else -> AgentSetupFileAction.UPDATE
}

@Composable
internal fun AgentSetupFileDiffDialog(edit: AgentSetupEdit, onDismiss: () -> Unit) {
    val diff = rememberTextDiff(edit.path, edit.before, edit.after)
    val clipboard = if (LocalInspectionMode.current) null else koinInject<ClipboardService>()
    DialogPopup(onDismissRequest = onDismiss, maxWidth = screenMaxWidth) {
        AgentSetupFileDiffDialogContent(
            edit = edit,
            diff = diff,
            onCopy = diff?.takeIf { it.patch.isNotEmpty() }?.let {
                { clipboard?.setPrimaryClip(it.patch, concealed = false) }
            },
            onDismiss = onDismiss,
        )
    }
}

/** Keep file identity and actions fixed while the selectable diff scrolls independently. */
@Composable
internal fun AgentSetupFileDiffDialogContent(
    edit: AgentSetupEdit,
    diff: PreparedTextDiff?,
    onCopy: (() -> Unit)?,
    onDismiss: () -> Unit,
) {
    DialogContent(
        title = {
            Text(stringResource(Res.string.text_diff_title))
            SelectionContainer {
                Text(
                    text = edit.path,
                    modifier = Modifier.padding(vertical = Dimens.verticalPadding),
                    style = MiuixTheme.textStyles.body2,
                    fontFamily = FontFamily.Monospace,
                )
            }
            Text(
                stringResource(edit.action.text),
                style = MiuixTheme.textStyles.footnote1,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            )
        },
        content = {
            TextDiff(
                model = diff?.model,
                modifier = Modifier.padding(horizontal = Dimens.horizontalPadding),
            )
        },
        actions = {
            TextButton(
                text = stringResource(Res.string.text_diff_copy),
                onClick = { onCopy?.invoke() },
                enabled = onCopy != null,
            )
            TextButton(
                text = stringResource(Res.string.close),
                onClick = onDismiss,
            )
        },
    )
}

@Preview
@Composable
internal fun AgentSetupFileDiffDialogPreview() {
    WizardPreview {
        Column {
            AgentSetupFileDiffDialog(
                edit = AgentSetupEdit(
                    path = "/home/alex/.zshrc",
                    before = "# Terminal configuration\nexport SSH_AUTH_SOCK=/tmp/old-agent.sock\n",
                    after = "# Terminal configuration\nexport SSH_AUTH_SOCK=/tmp/keyguard/ssh-agent.sock\n",
                ),
                onDismiss = {},
            )
        }
    }
}
