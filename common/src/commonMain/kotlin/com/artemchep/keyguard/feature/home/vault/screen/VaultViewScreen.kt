@file:OptIn(ExperimentalMaterial3Api::class)

package com.artemchep.keyguard.feature.home.vault.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import androidx.compose.material3.LocalAbsoluteTonalElevation
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.LocalAppMode
import com.artemchep.keyguard.feature.EmptyView
import com.artemchep.keyguard.feature.home.vault.component.VaultItemIcon2
import com.artemchep.keyguard.feature.home.vault.component.VaultViewItem
import com.artemchep.keyguard.feature.home.vault.component.rememberSecretAccentColor
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevationSemi
import com.artemchep.keyguard.feature.home.vault.model.VaultItemIcon
import com.artemchep.keyguard.feature.localization.textResource
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.Avatar
import com.artemchep.keyguard.ui.DefaultFab
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
import com.artemchep.keyguard.ui.FabState
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.FlatItemLayout
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.OptionallyKeepScreenOnEffect
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.shimmer.shimmer
import com.artemchep.keyguard.ui.skeleton.SkeletonText
import com.artemchep.keyguard.ui.text.AutoSizeText
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import com.artemchep.keyguard.ui.tooltip.Tooltip
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.KgShapes
import com.artemchep.keyguard.ui.theme.titleSmall
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevation

@Composable
fun VaultViewScreen(
    itemId: String,
    accountId: String,
) {
    OptionallyKeepScreenOnEffect()

    val state = vaultViewScreenState(
        mode = LocalAppMode.current,
        contentColor = LocalContentColor.current,
        disabledContentColor = LocalContentColor.current
            .combineAlpha(MediumEmphasisAlpha),
        itemId = itemId,
        accountId = accountId,
    )
    VaultViewScreenContent(
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun VaultViewScreenContent(
    state: VaultViewState,
) {
    val listState = rememberSaveable(
        0,
        saver = LazyListState.Saver,
    ) {
        LazyListState(
            firstVisibleItemIndex = 0,
            firstVisibleItemScrollOffset = 0,
        )
    }
    val scrollBehavior = ToolbarBehavior.behavior()
    ScaffoldLazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        expressive = true,
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = state.toVaultViewHeaderState().name.orEmpty(),
                navigationIcon = {
                    NavigationIcon()
                },
                actions = {
                    VaultViewTitleActions(
                        state = state,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionState = run {
            val action = (state.content as? VaultViewState.Content.Cipher)
                ?.primaryAction
            val fabState = if (action != null) {
                FabState(
                    onClick = action.onClick,
                    model = action,
                )
            } else {
                null
            }
            rememberUpdatedState(newValue = fabState)
        },
        floatingActionButton = {
            val action = this.state.value?.model as? FlatItemAction
            if (action != null) {
                DefaultFab(
                    icon = {
                        action.leading?.invoke()
                    },
                    text = {
                        Text(
                            text = textResource(action.title),
                        )
                    },
                )
            }
        },
        listState = listState,
    ) {
        when (state.content) {
            is VaultViewState.Content.Loading -> {
                // Show a bunch of skeleton items, so it makes an impression of a
                // fully loaded screen.
                for (i in 0..1) {
                    item(i) {
                        val contentColor =
                            LocalContentColor.current.copy(alpha = DisabledEmphasisAlpha)
                        FlatItemLayout(
                            modifier = Modifier
                                .shimmer(),
                            backgroundColor = contentColor.copy(alpha = 0.08f),
                            content = {
                                Box(
                                    Modifier
                                        .height(13.dp)
                                        .fillMaxWidth(0.15f)
                                        .clip(KgShapes.medium)
                                        .background(contentColor.copy(alpha = 0.2f)),
                                )
                                Box(
                                    Modifier
                                        .padding(top = 4.dp)
                                        .height(18.dp)
                                        .fillMaxWidth(0.38f)
                                        .clip(KgShapes.medium)
                                        .background(contentColor),
                                )
                            },
                        )
                    }
                }
                item(-1) {
                    val contentColor =
                        LocalContentColor.current.copy(alpha = DisabledEmphasisAlpha)
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            Modifier
                                .shimmer()
                                .height(10.dp)
                                .fillMaxWidth(0.38f)
                                .clip(KgShapes.medium)
                                .background(contentColor.copy(alpha = 0.23f)),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            Modifier
                                .shimmer()
                                .height(10.dp)
                                .fillMaxWidth(0.38f)
                                .clip(KgShapes.medium)
                                .background(contentColor.copy(alpha = 0.23f)),
                        )
                    }
                }
            }

            is VaultViewState.Content.Cipher -> {
                val list = state.content.items
                if (list.isEmpty()) {
                    item("header.empty") {
                        NoItemsPlaceholder()
                    }
                }
                // Group rows into Miuix cards: a SmallTitle per section and one
                // card per run of rows.
                val groups = run {
                    val result = mutableListOf<VaultViewGroup>()
                    var card: MutableList<VaultViewItem>? = null
                    list.forEach { model ->
                        if (model is VaultViewItem.Section) {
                            card = null
                            result.add(VaultViewGroup.SectionItem(model))
                        } else {
                            val current = card
                            if (current == null) {
                                val next = mutableListOf(model)
                                card = next
                                result.add(VaultViewGroup.CardGroup(next))
                            } else {
                                current.add(model)
                            }
                        }
                    }
                    result
                }
                items(
                    items = groups,
                    key = { group -> group.id },
                ) { group ->
                    when (group) {
                        is VaultViewGroup.SectionItem -> {
                            val text = group.item.text
                            if (text != null) {
                                SmallTitle(
                                    text = text,
                                )
                            }
                        }

                        is VaultViewGroup.CardGroup -> {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 12.dp),
                            ) {
                                group.items.forEach { model ->
                                    VaultViewItem(
                                        modifier = Modifier,
                                        item = model,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                // Do nothing.
            }
        }
    }
}

@Composable
private fun VaultViewTitle(
    state: VaultViewState,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
) {
    val header = state.toVaultViewHeaderState()
    val shimmerColor = LocalContentColor.current
        .combineAlpha(DisabledEmphasisAlpha)

    val avatarBackground = when (state.content) {
        is VaultViewState.Content.Loading -> shimmerColor
        is VaultViewState.Content.NotFound -> shimmerColor
        is VaultViewState.Content.Cipher -> {
            val avatarIcon = header.icon
            val avatarBackground = if (
                (avatarIcon !is VaultItemIcon.VectorIcon &&
                        avatarIcon !is VaultItemIcon.TextIcon) ||
                !header.hasRemoteService
            ) {
                val elevation = LocalAbsoluteTonalElevation.current + 8.dp
                MiuixTheme.colorScheme
                    .surfaceColorAtElevationSemi(elevation = elevation)
                    .combineAlpha(LocalContentColor.current.alpha)
            } else {
                rememberSecretAccentColor(
                    accentLight = header.accentLight,
                    accentDark = header.accentDark,
                )
            }
            avatarBackground
        }
    }
    Avatar(
        modifier = Modifier
            .then(
                if (header.isLoading) {
                    Modifier
                        .shimmer()
                } else {
                    Modifier
                },
            ),
        color = avatarBackground,
    ) {
        val icon = header.icon
        if (icon != null) {
            VaultItemIcon2(
                icon,
                modifier = Modifier
                    .alpha(LocalContentColor.current.alpha),
            )
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
    when (state.content) {
        is VaultViewState.Content.Loading -> {
            SkeletonText(
                modifier = Modifier
                    .fillMaxWidth(0.33f),
                emphasis = MediumEmphasisAlpha,
            )
            // Apparently a text has some sort of margins?
            Text("")
        }

        is VaultViewState.Content.Cipher -> {
            val title = header.name
                ?.takeUnless { it.isEmpty() }
            Tooltip(
                valueOrNull = title,
                tooltip = { text ->
                    Text(
                        text,
                        maxLines = 8,
                    )
                },
            ) {
                if (title != null) {
                    AutoSizeText(
                        text = title,
                        maxLines = 2,
                        minTextSize = MiuixTheme.textStyles.titleSmall.fontSize,
                        maxTextSize = LocalTextStyle.current.fontSize,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.empty_value),
                        color = LocalContentColor.current
                            .combineAlpha(DisabledEmphasisAlpha),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        is VaultViewState.Content.NotFound -> {
            Text(
                text = "Not found",
                color = MiuixTheme.colorScheme.error
                    .combineAlpha(LocalContentColor.current.alpha),
            )
        }
    }
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
private fun RowScope.VaultViewTitleActions(
    state: VaultViewState,
) {
    val content = state.content as? VaultViewState.Content.Cipher
        ?: return
    VaultViewCipherTitleActions(
        state = content,
    )
}

@Composable
fun NoItemsPlaceholder() {
    EmptyView(
        icon = {
            Icon(Icons.Outlined.SearchOff, null)
        },
        text = {
            Text(text = "No items")
        },
    )
}

private sealed interface VaultViewGroup {
    val id: String

    data class SectionItem(
        val item: VaultViewItem.Section,
    ) : VaultViewGroup {
        override val id: String get() = "section:${item.id}"
    }

    data class CardGroup(
        val items: List<VaultViewItem>,
    ) : VaultViewGroup {
        override val id: String get() = "card:${items.first().id}"
    }
}
