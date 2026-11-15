package com.artemchep.keyguard.feature.agent.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.agent.help.AgentSetupCodeBlock
import com.artemchep.keyguard.feature.dialog.Dialog
import com.artemchep.keyguard.feature.home.vault.component.VaultItemReference
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.ContextItem
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.TableRowItem
import com.artemchep.keyguard.ui.icons.icon
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.theme.monoFontFamily
import com.artemchep.keyguard.ui.theme.okContainer
import com.artemchep.keyguard.ui.theme.onOkContainer
import com.artemchep.keyguard.ui.util.HorizontalDivider
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AgentHistoryDetailsScreen(
    details: AgentHistoryDetails,
) {
    val loadableState = produceAgentHistoryDetailsState(
        details = details,
    )
    AgentHistoryDetailsScreen(
        details = details,
        loadableState = loadableState,
    )
}

@Composable
private fun AgentHistoryDetailsScreen(
    details: AgentHistoryDetails,
    loadableState: Loadable<AgentHistoryDetailsState>,
) {
    val state = loadableState.getOrNull()
    Dialog(
        icon = icon(details.icon.imageVector()),
        title = {
            Text(
                text = details.title,
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
            AgentHistoryOutcomeBadge(
                outcome = details.outcome,
                text = details.outcomeText,
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                state?.items?.forEach { item ->
                    AgentHistoryDetailsItem(
                        item = item,
                    )
                }
                AgentHistoryDetailsDate(
                    text = details.formattedDate,
                )
            }
        },
        contentScrollable = true,
        actions = {
            TextButton(
                text = stringResource(Res.string.close),
                enabled = state != null,
                onClick = {
                    state?.onClose?.invoke()
                },
            )
        },
    )
}

/** When the request was made. */
@Composable
private fun AgentHistoryDetailsDate(
    text: String,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimens.horizontalPadding,
                end = Dimens.horizontalPadding,
                top = 16.dp,
            ),
        text = text,
        textAlign = TextAlign.Center,
        style = MiuixTheme.textStyles.footnote1,
        color = LocalContentColor.current
            .combineAlpha(MediumEmphasisAlpha),
    )
}

@Composable
private fun AgentHistoryDetailsItem(
    item: AgentHistoryDetailsItem,
) = when (item) {
    is AgentHistoryDetailsItem.Section -> if (item.title != null) {
        SmallTitle(text = item.title)
    } else {
        Spacer(
            modifier = Modifier
                .height(Dimens.verticalPadding),
        )
    }

    is AgentHistoryDetailsItem.Table -> AgentHistoryDetailsTableItem(
        item = item,
    )

    is AgentHistoryDetailsItem.Value -> when (val row = item.row) {
        is AgentHistoryDetails.Row.Text -> AgentHistoryDetailsTextItem(
            item = item,
            row = row,
        )

        is AgentHistoryDetails.Row.Code -> AgentHistoryDetailsCodeItem(
            row = row,
        )

        is AgentHistoryDetails.Row.Reference -> AgentHistoryDetailsReferenceItem(
            item = item,
            row = row,
        )
    }
}

@Composable
private fun AgentHistoryDropdownRow(
    dropdown: List<ContextItem>,
    startAction: (@Composable () -> Unit)? = null,
    endActions: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    Box {
        BasicComponent(
            startAction = startAction,
            content = content,
            endActions = {
                endActions?.invoke(this)
                if (dropdown.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            dropdownExpanded = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            },
            onClick = onClick,
        )
        KeyguardDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = {
                dropdownExpanded = false
            },
        ) {
            dropdown.forEach { action ->
                DropdownMenuItemFlat(
                    action = action,
                )
            }
        }
    }
}

@Composable
private fun AgentHistoryDetailsTextItem(
    item: AgentHistoryDetailsItem.Value,
    row: AgentHistoryDetails.Row.Text,
) {
    AgentHistoryDropdownRow(
        dropdown = item.dropdown,
        startAction = row.icon?.let { icon ->
            // composable
            {
                Icon(
                    imageVector = icon.imageVector(),
                    contentDescription = null,
                )
            }
        },
        endActions = {
            val onCopy = remember(item.dropdown) {
                item.dropdown
                    .firstNotNullOfOrNull { it as? FlatItemAction }
                    ?.takeIf { it.type == FlatItemAction.Type.COPY }
                    ?.onClick
            }
            if (onCopy != null) {
                IconButton(
                    onClick = onCopy,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = stringResource(Res.string.copy),
                    )
                }
            }
        },
    ) {
        Text(row.label)
        Text(
            text = row.value,
            style = MiuixTheme.textStyles.body2,
            fontFamily = if (row.monospace) monoFontFamily else null,
            color = LocalContentColor.current
                .combineAlpha(MediumEmphasisAlpha),
        )
    }
}

/** The labels and values of a section, as one table. */
@Composable
private fun AgentHistoryDetailsTableItem(
    item: AgentHistoryDetailsItem.Table,
) {
    AgentHistoryDropdownRow(
        dropdown = item.dropdown,
    ) {
        item.rows.forEachIndexed { index, row ->
            TableRowItem(
                modifier = Modifier
                    .fillMaxWidth(),
                title = row.label,
                text = row.value,
            )
            if (index < item.rows.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun AgentHistoryDetailsCodeItem(
    row: AgentHistoryDetails.Row.Code,
) {
    // The code block has a copy button of its own.
    AgentSetupCodeBlock(
        text = row.value,
    )
}

@Composable
private fun AgentHistoryDetailsReferenceItem(
    item: AgentHistoryDetailsItem.Value,
    row: AgentHistoryDetails.Row.Reference,
) {
    BasicComponent(
        content = {
            VaultItemReference(
                item = row.item,
                field = row.label,
                chevron = item.onClick != null,
            )
        },
        onClick = item.onClick,
    )
}

@Composable
private fun AgentHistoryOutcomeBadge(
    outcome: AgentHistoryOutcome,
    text: String,
) {
    val colors = outcome.colors()
    Surface(
        color = colors.container,
        contentColor = colors.onContainer,
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 12.dp,
                    top = 4.dp,
                    bottom = 4.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(18.dp),
                imageVector = when (outcome) {
                    AgentHistoryOutcome.SUCCESS -> Icons.Outlined.CheckCircle
                    AgentHistoryOutcome.FAILURE -> Icons.Outlined.Cancel
                    AgentHistoryOutcome.NEUTRAL -> Icons.Outlined.Info
                },
                contentDescription = null,
            )
            Text(
                text = text,
                style = MiuixTheme.textStyles.footnote1,
            )
        }
    }
}

private class AgentHistoryOutcomeColors(
    val container: Color,
    val onContainer: Color,
)

@Composable
private fun AgentHistoryOutcome.colors() = when (this) {
    AgentHistoryOutcome.SUCCESS -> AgentHistoryOutcomeColors(
        container = MiuixTheme.colorScheme.okContainer,
        onContainer = MiuixTheme.colorScheme.onOkContainer,
    )

    AgentHistoryOutcome.FAILURE -> AgentHistoryOutcomeColors(
        container = MiuixTheme.colorScheme.errorContainer,
        onContainer = MiuixTheme.colorScheme.onErrorContainer,
    )

    AgentHistoryOutcome.NEUTRAL -> AgentHistoryOutcomeColors(
        container = MiuixTheme.colorScheme.surfaceContainerHigh,
        onContainer = MiuixTheme.colorScheme.onSurfaceVariantSummary,
    )
}
