package com.artemchep.keyguard.feature.generator.emailrelay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.model.flatMap
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.EmptyView
import com.artemchep.keyguard.feature.ErrorView
import com.artemchep.keyguard.feature.home.vault.component.rememberSecretAccentColor
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.AvatarBuilder
import com.artemchep.keyguard.ui.DefaultFab
import com.artemchep.keyguard.ui.DefaultSelection
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.DropdownMinWidth
import com.artemchep.keyguard.ui.DropdownScopeImpl
import com.artemchep.keyguard.ui.ExpandedIfNotEmptyForRow
import com.artemchep.keyguard.ui.FabState
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.RequestLazyListScrollOnRevision
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.icons.IconBox
import com.artemchep.keyguard.ui.skeleton.SkeletonItem
import com.artemchep.keyguard.ui.skeleton.skeletonItems
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import com.artemchep.keyguard.ui.util.DividerColor
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.KgShapes
import androidx.compose.ui.state.ToggleableState
import com.artemchep.keyguard.ui.theme.labelSmall

@Composable
fun EmailRelayListScreen(
) {
    val loadableState = produceEmailRelayListState(
    )
    EmailRelayListScreen(
        loadableState = loadableState,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EmailRelayListScreen(
    loadableState: Loadable<EmailRelayListState>,
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

    val dp = remember {
        mutableStateOf(false)
    }
    val rp = remember {
        mutableStateOf(false)
    }
    val pitems = loadableState.getOrNull()?.content?.getOrNull()?.getOrNull()?.primaryActions
    if (pitems.isNullOrEmpty()) {
        dp.value = false
    }

    ScaffoldLazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        expressive = true,
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = stringResource(Res.string.emailrelay_list_header_title),
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
        floatingActionState = run {
            val fabVisible = !pitems.isNullOrEmpty()
            val fabState = if (fabVisible) {
                FabState(
                    onClick = {
                        dp.value = true
                    },
                    model = null,
                )
            } else {
                null
            }
            rememberUpdatedState(newValue = fabState)
        },
        floatingActionButton = {
            DefaultFab(
                icon = {
                    IconBox(main = Icons.Outlined.Add)

                    // Inject the dropdown popup to the bottom of the
                    // content.
                    val onDismissRequest = remember(dp) {
                        // lambda
                        {
                            dp.value = false
                        }
                    }
                    KeyguardDropdownMenu(
                        expanded = dp.value,
                        onDismissRequest = onDismissRequest,
                    ) {
                        pitems?.forEachIndexed { index, action ->
                            DropdownMenuItemFlat(
                                action = action,
                            )
                        }
                    }
                },
                text = {
                    Text(
                        text = stringResource(Res.string.add_integration),
                    )
                },
            )
        },
        listState = listState,
    ) {
        val contentState = loadableState
            .flatMap { it.content }
        when (contentState) {
            is Loadable.Loading -> {
                skeletonItems()
            }

            is Loadable.Ok -> {
                contentState.value.fold(
                    ifLeft = { e ->
                        item("error") {
                            ErrorView(
                                text = {
                                    Text(text = "Failed to load app list!")
                                },
                                exception = e,
                            )
                        }
                    },
                    ifRight = { content ->
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
                            AppItem(
                                modifier = Modifier
                                    .animateItem(),
                                item = item,
                            )
                        }
                    },
                )
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
                text = stringResource(Res.string.emailrelay_empty_label),
            )
        },
    )
}

@Composable
private fun AppItem(
    modifier: Modifier,
    item: EmailRelayListState.Item,
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
            startAction = {
                val accent = rememberSecretAccentColor(
                    accentLight = item.accentLight,
                    accentDark = item.accentDark,
                )
                AvatarBuilder(
                    icon = item.icon,
                    accent = accent,
                    active = true,
                    badge = {
                        // Do nothing.
                    },
                )
            },
            content = {
                FlatItemTextContent(
                    title = {
                        Text(item.title)
                    },
                )
            },
            endActions = {
                Text(
                    modifier = Modifier
                        .widthIn(max = 128.dp)
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp,
                        )
                        .border(
                            Dp.Hairline,
                            DividerColor,
                            KgShapes.small,
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        ),
                    text = item.service,
                    style = MiuixTheme.textStyles.labelSmall,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                )
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
