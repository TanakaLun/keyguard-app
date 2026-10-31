package com.artemchep.keyguard.feature.attachments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.material3.TopAppBarDefaults
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.model.ShapeState
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.EmptySearchView
import com.artemchep.keyguard.feature.attachments.compose.ItemAttachment
import com.artemchep.keyguard.feature.watchtower.VaultHomeScreenFilterButton2
import com.artemchep.keyguard.feature.watchtower.VaultHomeScreenFilterPaneCard2
import com.artemchep.keyguard.feature.home.vault.component.Section
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.feature.twopane.TwoPaneScreen
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DefaultSelection
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.skeleton.SkeletonItem
import com.artemchep.keyguard.ui.skeleton.skeletonItems
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.SmallToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import org.jetbrains.compose.resources.stringResource

@Composable
fun AttachmentsScreen() {
    val loadableState = produceAttachmentsScreenState()
    val scrollBehavior = ToolbarBehavior.behavior()
    TwoPaneScreen(
        header = { modifier ->
            SmallToolbar(
                modifier = modifier,
                title = stringResource(Res.string.downloads),
                navigationIcon = {
                    NavigationIcon()
                },
            )

            SideEffect {
                if (scrollBehavior.state.heightOffsetLimit != 0f) {
                    scrollBehavior.state.heightOffsetLimit = 0f
                }
            }
        },
        detail = { modifier ->
            VaultHomeScreenFilterPaneCard2(
                modifier = modifier,
                items = loadableState.getOrNull()?.filter?.items.orEmpty(),
                onClear = loadableState.getOrNull()?.filter?.onClear,
            )
        },
    ) { modifier, tabletUi ->
        AttachmentsScreen(
            modifier = modifier,
            state = loadableState,
            tabletUi = tabletUi,
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun AttachmentsScreen(
    modifier: Modifier,
    state: Loadable<AttachmentsState>,
    tabletUi: Boolean,
    scrollBehavior: ScrollBehavior,
) {
    val itemList = state.getOrNull()?.items.orEmpty()

    // One section title per section, and one Card per group of rows.
    val groups = remember(itemList) {
        buildList<AttachmentsGroup> {
            var card: MutableList<AttachmentsState.Item.Attachment>? = null
            itemList.forEach { value ->
                when (value) {
                    is AttachmentsState.Item.Section -> {
                        card = null
                        add(
                            AttachmentsGroup.Section(
                                key = value.key,
                                name = value.name,
                            ),
                        )
                    }

                    is AttachmentsState.Item.Attachment -> {
                        val current = card
                        if (current == null) {
                            val next = mutableListOf(value)
                            card = next
                            add(AttachmentsGroup.Card(next))
                        } else {
                            current.add(value)
                        }
                    }
                }
            }
        }
    }

    ScaffoldLazyColumn(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        expressive = true,
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            if (tabletUi) {
                return@ScaffoldLazyColumn
            }

            LargeToolbar(
                title = stringResource(Res.string.downloads),
                navigationIcon = {
                    NavigationIcon()
                },
                actions = {
                    VaultHomeScreenFilterButton2(
                        modifier = modifier,
                        items = state.getOrNull()?.filter?.items.orEmpty(),
                        onClear = state.getOrNull()?.filter?.onClear,
                        onSave = state.getOrNull()?.filter?.onSave,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            val selectionOrNull = state.getOrNull()?.selection
            DefaultSelection(
                state = selectionOrNull,
            )
        },
    ) {
        when (state) {
            is Loadable.Loading -> {
                skeletonItems()
            }

            is Loadable.Ok -> {
                if (itemList.isEmpty()) {
                    item("header.empty") {
                        NoItemsPlaceholder()
                    }
                }

                items(groups, key = { it.key }) { group ->
                    when (group) {
                        is AttachmentsGroup.Section -> Section(
                            text = group.name,
                        )

                        is AttachmentsGroup.Card -> Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 12.dp),
                        ) {
                            group.items.forEach { value ->
                                ItemAttachment(
                                    modifier = Modifier,
                                    item = value.item,
                                    shapeState = ShapeState.ALL,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private sealed interface AttachmentsGroup {
    val key: String

    data class Section(
        override val key: String,
        val name: String,
    ) : AttachmentsGroup

    data class Card(
        val items: List<AttachmentsState.Item.Attachment>,
    ) : AttachmentsGroup {
        override val key: String get() = "card:${items.first().key}"
    }
}

@Composable
private fun NoItemsPlaceholder(
    modifier: Modifier = Modifier,
) {
    EmptySearchView(
        modifier = modifier,
        text = {
            Text(
                text = stringResource(Res.string.downloads_empty_label),
            )
        },
    )
}
