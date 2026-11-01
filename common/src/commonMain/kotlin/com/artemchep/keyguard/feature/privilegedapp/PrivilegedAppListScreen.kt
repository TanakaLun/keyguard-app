@file:OptIn(ExperimentalMaterial3Api::class)

package com.artemchep.keyguard.feature.privilegedapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.LocalTextStyle
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.EmptyView
import com.artemchep.keyguard.feature.ErrorView
import com.artemchep.keyguard.feature.home.vault.component.Section
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.feature.urloverride.MiniRow
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DefaultFab
import com.artemchep.keyguard.ui.DefaultSelection
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.ExpandedIfNotEmptyForRow
import com.artemchep.keyguard.ui.FabState
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.RequestLazyListScrollOnRevision
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.icons.IconBox
import com.artemchep.keyguard.ui.skeleton.SkeletonItemAvatar
import com.artemchep.keyguard.ui.skeleton.skeletonItems
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.bodyMedium
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.state.ToggleableState

@Composable
fun PrivilegedAppListScreen() {
    val loadableState = producePrivilegedAppListState(
    )
    PrivilegedAppListScreen(
        loadableState = loadableState,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PrivilegedAppListScreen(
    loadableState: Loadable<PrivilegedAppListState>,
) {
    val scrollBehavior = ToolbarBehavior.behavior()

    val listRevision =
        loadableState.getOrNull()?.content?.getOrNull()?.getOrNull()?.revision
    val listState = remember {
        LazyListState(
            firstVisibleItemIndex = 0,
            firstVisibleItemScrollOffset = 0,
        )
    }

    RequestLazyListScrollOnRevision(
        listState = listState,
        revision = listRevision,
    )

    ScaffoldLazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        expressive = true,
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = stringResource(Res.string.privilegedapps_list_header_title),
                navigationIcon = {
                    NavigationIcon()
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            val selectionOrNull =
                loadableState.getOrNull()?.content?.getOrNull()?.getOrNull()?.selection
            DefaultSelection(
                state = selectionOrNull,
            )
        },
        listState = listState,
    ) {
        val contentState = loadableState.toPrivilegedAppListContentState()
        when (contentState) {
            is PrivilegedAppListContentState.Loading -> {
                skeletonItems(
                    avatar = SkeletonItemAvatar.LARGE,
                    count = 1, // usually there's less than one
                )
            }

            is PrivilegedAppListContentState.Error -> {
                item("error") {
                    ErrorView(
                        text = {
                            Text(text = "Failed to load privileged app list!")
                        },
                        exception = contentState.exception,
                    )
                }
            }

            is PrivilegedAppListContentState.Content -> {
                val content = contentState.content
                val items = content.items
                if (items.isEmpty()) {
                    item("empty") {
                        NoItemsPlaceholder()
                    }
                }

                items(
                    items = items,
                    key = { it.key },
                ) { item ->
                    PrivilegedAppItem(
                        modifier = Modifier
                            .animateItem(),
                        item = item,
                    )
                }
            }
        }
    }
}

@Composable
private fun NoItemsPlaceholder(
    modifier: Modifier = Modifier,
) {
    EmptyView(
        modifier = modifier,
        text = {
            Text(
                text = stringResource(Res.string.privilegedapps_empty_label),
            )
        },
    )
}

@Composable
private fun PrivilegedAppItem(
    modifier: Modifier,
    item: PrivilegedAppListState.Item,
) = PrivilegedAppItemContent(
    modifier = modifier,
    item = item,
    renderers = commonPrivilegedAppItemRenderers,
)

private val commonPrivilegedAppItemRenderers = PrivilegedAppItemRenderers(
    section = { modifier, item ->
        Section(
            modifier = modifier,
            text = item.name,
        )
    },
    content = { modifier, item ->
        PrivilegedAppItemContentRow(
            modifier = modifier,
            item = item,
        )
    },
)

@Composable
private fun PrivilegedAppItemContentRow(
    modifier: Modifier,
    item: PrivilegedAppListState.Item.Content,
) {
    val selectableState by item.selectableState.collectAsState()
    val backgroundColor = when {
        selectableState.selected -> MiuixTheme.colorScheme.primaryContainer
        else -> Color.Unspecified
    }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val onLongClick = selectableState.onLongClick
    val onClick: (() -> Unit)? = when {
        selectableState.onClick != null -> selectableState.onClick
        item.dropdown.isNotEmpty() -> {
            // lambda
            {
                dropdownExpanded = true
            }
        }

        else -> null
    }
    Box {
        BasicComponent(
            modifier = modifier
                .then(
                    if (backgroundColor.isSpecified) {
                        Modifier.background(backgroundColor)
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (onClick != null || onLongClick != null) {
                        Modifier.combinedClickable(
                            onClick = onClick ?: {},
                            onLongClick = onLongClick,
                        )
                    } else {
                        Modifier
                    },
                ),
            content = {
                Text(
                    text = item.title,
                    style = MiuixTheme.textStyles.bodyLarge,
                )
                CompositionLocalProvider(
                    LocalTextStyle provides MiuixTheme.textStyles.bodyMedium
                        .copy(
                            color = LocalContentColor.current
                                .combineAlpha(MediumEmphasisAlpha),
                        ),
                ) {
                    Column {
                        if (item.cert.isNotEmpty()) {
                            MiniRow(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                icon = Icons.Outlined.Fingerprint,
                                text = item.cert,
                            )
                        }
                    }
                }
            },
            endActions = {
                ExpandedIfNotEmptyForRow(
                    selectableState.selected.takeIf { selectableState.selecting },
                ) { selected ->
                    Checkbox(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        state = ToggleableState(selected),
                        onClick = null,
                    )
                }
            },
        )
        KeyguardDropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = {
                dropdownExpanded = false
            },
        ) {
            item.dropdown.forEach { action ->
                DropdownMenuItemFlat(
                    action = action,
                )
            }
        }
    }
}
