@file:OptIn(ExperimentalMaterial3Api::class)

package com.artemchep.keyguard.feature.home.vault.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.feature.EmptyView
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevationSemi
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.AhLayout
import com.artemchep.keyguard.ui.DefaultFab
import com.artemchep.keyguard.ui.DefaultSelection
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.ExpandedIfNotEmptyForRow
import com.artemchep.keyguard.ui.FabState
import com.artemchep.keyguard.ui.FlatDropdown
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.animatedNumberText
import com.artemchep.keyguard.ui.skeleton.SkeletonItemPilled
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.state.ToggleableState
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevation

@Composable
fun CollectionsScreen(
    args: CollectionsRoute.Args,
) {
    val state = collectionsScreenState(
        args = args,
    )
    CollectionsScreenContent(
        state = state,
    )
}

@Composable
fun CollectionsScreenContent(
    state: CollectionsState,
) {
    val scrollBehavior = ToolbarBehavior.behavior()
    ScaffoldLazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        expressive = true,
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = stringResource(Res.string.account),
                navigationIcon = {
                    NavigationIcon()
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionState = run {
            val fabOnClick = state.onAdd
            val fabState = if (fabOnClick != null) {
                FabState(
                    onClick = fabOnClick,
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
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                    )
                },
                text = {
                    Text("New collection")
                },
            )
        },
        bottomBar = {
            DefaultSelection(
                state = state.selection,
            )
        },
    ) {
        when (val contentState = state.content) {
            is Loadable.Loading -> {
                for (i in 1..3) {
                    item("skeleton.$i") {
                        SkeletonItemPilled()
                    }
                }
            }

            is Loadable.Ok -> {
                val items = contentState.value.items
                if (items.isEmpty()) {
                    item("empty") {
                        EmptyView(
                            text = {
                                Text(
                                    text = stringResource(Res.string.collections_empty_label),
                                )
                            },
                        )
                    }
                }

                items(
                    items = items,
                    key = { it.key },
                ) {
                    OrganizationsScreenItem(
                        modifier = Modifier
                            .animateItem(),
                        item = it,
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizationsScreenItem(
    modifier: Modifier = Modifier,
    item: CollectionsState.Content.Item,
) = when (item) {
    is CollectionsState.Content.Item.Section ->
        OrganizationsScreenSectionItem(
            modifier = modifier,
            item = item,
        )

    is CollectionsState.Content.Item.Collection ->
        OrganizationsScreenCollectionItem(
            modifier = modifier,
            item = item,
        )
}

@Composable
private fun OrganizationsScreenSectionItem(
    modifier: Modifier = Modifier,
    item: CollectionsState.Content.Item.Section,
) {
    val text = item.text
    if (text != null) {
        SmallTitle(
            modifier = modifier,
            text = text,
        )
    } else {
        HorizontalDivider(
            modifier = modifier
                .padding(
                    vertical = 8.dp,
                ),
        )
    }
}

@Composable
private fun OrganizationsScreenCollectionItem(
    modifier: Modifier = Modifier,
    item: CollectionsState.Content.Item.Collection,
) {
    val backgroundColor =
        if (item.selected) MiuixTheme.colorScheme.primaryContainer else Color.Unspecified
    var dropdownExpanded by remember { mutableStateOf(false) }
    val onLongClick = item.onLongClick
    val onClick: (() -> Unit)? = when {
        item.onClick != null -> item.onClick
        item.actions.isNotEmpty() -> {
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
                Row {
                    val pillElevation = LocalAbsoluteTonalElevation.current + 8.dp
                    val pillColor = MiuixTheme.colorScheme
                        .surfaceColorAtElevationSemi(elevation = pillElevation)
                    AhLayout(
                        modifier = modifier,
                        backgroundColor = pillColor,
                    ) {
                        val text = animatedNumberText(item.ciphers)
                        Text(text)
                    }
                }
            },
            content = {
                Text(
                    text = item.title,
                    style = MiuixTheme.textStyles.bodyLarge,
                )
            },
            endActions = {
                ExpandedIfNotEmptyForRow(
                    item.selected.takeIf { item.selecting },
                ) { selected ->
                    Checkbox(
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
            item.actions.forEach { action ->
                DropdownMenuItemFlat(
                    action = action,
                )
            }
        }
    }
}
