package com.artemchep.keyguard.feature.cliagent.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.Terminal
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.CliAgentUsageHistoryClient
import com.artemchep.keyguard.common.model.CliAgentUsageHistoryRequestType
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.model.fold
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.EmptyView
import com.artemchep.keyguard.feature.agent.history.contentColor
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.OptionsButton
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.icons.Stub
import com.artemchep.keyguard.ui.skeleton.SkeletonSection
import com.artemchep.keyguard.ui.skeleton.skeletonItems
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun CliAgentHistoryScreen(
    client: CliAgentUsageHistoryClient?,
    cipherId: String?,
) {
    val loadableState = produceCliAgentHistoryState(
        client = client,
        cipherId = cipherId,
    )
    CliAgentHistoryScreen(
        loadableState = loadableState,
    )
}

@Composable
private fun CliAgentHistoryScreen(
    loadableState: Loadable<CliAgentHistoryState>,
) {
    val scrollBehavior = ToolbarBehavior.behavior()
    ScaffoldLazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = stringResource(Res.string.pref_item_agent_history_title),
                subtitle = loadableState.getOrNull()?.subtitle.orEmpty(),
                navigationIcon = {
                    NavigationIcon()
                },
                actions = {
                    loadableState.fold(
                        ifLoading = {
                            // Empty
                        },
                        ifOk = { state ->
                            OptionsButton(state.options)
                        },
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        provideContentUserScrollEnabled = {
            loadableState !is Loadable.Loading
        },
    ) {
        loadableState.fold(
            ifLoading = {
                populateCliAgentHistorySkeleton()
            },
            ifOk = { state ->
                populateCliAgentHistoryContent(
                    items = state.items,
                )
            },
        )
    }
}

private fun LazyListScope.populateCliAgentHistorySkeleton() {
    item("skeleton.section") {
        SkeletonSection()
    }
    skeletonItems(
        count = 12,
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.populateCliAgentHistoryContent(
    items: ImmutableList<CliAgentHistoryItem>,
) {
    if (items.isEmpty()) {
        item("empty") {
            EmptyView()
        }
    }
    items(
        items = items,
        key = { it.id },
    ) { item ->
        CliAgentHistoryItem(
            modifier = Modifier
                .animateItem(),
            item = item,
        )
    }
}

@Composable
private fun CliAgentHistoryItem(
    modifier: Modifier,
    item: CliAgentHistoryItem,
) = when (item) {
    is CliAgentHistoryItem.Section -> if (item.text != null) {
        SmallTitle(
            text = item.text,
        )
    } else {
        Spacer(modifier = modifier)
    }

    is CliAgentHistoryItem.Value -> CliAgentHistoryValueItem(
        modifier = modifier,
        item = item,
    )
}

@Composable
private fun CliAgentHistoryValueItem(
    modifier: Modifier,
    item: CliAgentHistoryItem.Value,
) {
    BasicComponent(
        modifier = modifier,
        startAction = {
            Icon(
                imageVector = item.request.icon(),
                contentDescription = null,
            )
        },
        content = {
            Text(
                text = item.caller,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MiuixTheme.textStyles.body1,
            )
            Text(
                text = item.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MiuixTheme.textStyles.body2,
                color = LocalContentColor.current
                    .combineAlpha(MediumEmphasisAlpha),
            )
            if (item.commandText != null) {
                Text(
                    text = item.commandText,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    style = MiuixTheme.textStyles.footnote1,
                    color = LocalContentColor.current
                        .combineAlpha(MediumEmphasisAlpha),
                )
            }
            Text(
                text = item.formattedDate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MiuixTheme.textStyles.footnote1,
                color = LocalContentColor.current
                    .combineAlpha(MediumEmphasisAlpha),
            )
        },
        endActions = {
            Text(
                modifier = Modifier
                    .widthIn(max = 96.dp),
                text = item.responseText,
                color = item.responseColor(),
                style = MiuixTheme.textStyles.footnote1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        onClick = item.onClick,
        enabled = true,
    )
}

private fun CliAgentUsageHistoryRequestType.icon(): ImageVector = when (this) {
    CliAgentUsageHistoryRequestType.RUN -> Icons.Outlined.Terminal
    CliAgentUsageHistoryRequestType.MCP_AUTHENTICATE,
    CliAgentUsageHistoryRequestType.MCP_LIST_ENVIRONMENTS,
    CliAgentUsageHistoryRequestType.MCP_CREATE_ENVIRONMENT,
    CliAgentUsageHistoryRequestType.MCP_RENAME_ENVIRONMENT,
    CliAgentUsageHistoryRequestType.MCP_LIST_VARIABLES,
    CliAgentUsageHistoryRequestType.MCP_APPEND_VARIABLES,
    CliAgentUsageHistoryRequestType.MCP_CREATE_LOCAL_ENV_FILE,
    CliAgentUsageHistoryRequestType.MCP_LIST_LOCAL_ENV_FILES,
    -> Icons.Outlined.SmartToy

    CliAgentUsageHistoryRequestType.READ_LOCAL_ENV_FILE -> Icons.Outlined.Description
    CliAgentUsageHistoryRequestType.UNKNOWN -> Icons.Stub
}

@Composable
private fun CliAgentHistoryItem.Value.responseColor() = response
    .outcome()
    .contentColor()
