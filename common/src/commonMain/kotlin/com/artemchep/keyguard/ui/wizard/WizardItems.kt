package com.artemchep.keyguard.ui.wizard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.theme.bodySmall
import com.artemchep.keyguard.ui.theme.combineAlpha
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * One option of a single choice. Put the options
 * of a choice in a `Modifier.selectableGroup()`.
 */
@Composable
fun WizardChoice(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val backgroundColor = wizardSelectedBackground(selected)
    BasicComponent(
        modifier = Modifier
            .semantics {
                this.selected = selected
                role = Role.RadioButton
            }
            .then(
                if (backgroundColor.isSpecified) {
                    Modifier.background(backgroundColor)
                } else {
                    Modifier
                },
            ),
        title = title,
        startAction = { Icon(icon, contentDescription = null) },
        endActions = { RadioButton(selected = selected, onClick = null, enabled = enabled) },
        onClick = onClick.takeIf { enabled },
    )
}

/**
 * The background of a selected
 * or checked option.
 */
@Composable
fun wizardSelectedBackground(selected: Boolean): Color =
    if (selected) MiuixTheme.colorScheme.secondaryContainer else Color.Unspecified

/**
 * A row of the summary that a wizard
 * shows before its final action.
 */
@Composable
fun WizardSummaryRow(
    icon: ImageVector,
    title: String,
    value: String?,
) {
    Row(
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 120.dp),
            text = title,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 120.dp),
            text = value.orEmpty(),
            style = MiuixTheme.textStyles.bodySmall,
            color = LocalContentColor.current.combineAlpha(MediumEmphasisAlpha),
        )
    }
}
