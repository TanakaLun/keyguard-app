package com.artemchep.keyguard.feature.home.settings.accounts

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.EmptyView
import com.artemchep.keyguard.feature.auth.bitwarden.BitwardenLoginRouteFactory
import com.artemchep.keyguard.feature.home.settings.accounts.component.AccountListItem
import com.artemchep.keyguard.feature.home.settings.accounts.model.AccountItem
import com.artemchep.keyguard.feature.navigation.LocalNavigationController
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.feature.navigation.NavigationIntent
import com.artemchep.keyguard.feature.navigation.registerRouteResultReceiver
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.ui.DefaultFab
import com.artemchep.keyguard.ui.FabState
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.skeleton.SkeletonItem
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.currentKoinScope

@Composable
fun AccountListScreen() {
    val controller by rememberUpdatedState(LocalNavigationController.current)
    val bitwardenLoginRouteFactory = currentKoinScope().get<BitwardenLoginRouteFactory>()
    val r = registerRouteResultReceiver(bitwardenLoginRouteFactory.create()) {
        controller.queue(NavigationIntent.Pop)
    }

    val accountListStateWrapper = accountListScreenState(
        rootRoute = AccountsRoute,
    )
    val accountListState = remember(accountListStateWrapper) {
        accountListStateWrapper.unwrap(
            onAddAccount = {
                controller.queue(NavigationIntent.NavigateToRoute(r))
            },
        )
    }
    AccountListScreenContent(
        state = accountListState,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun AccountListScreenContent(
    state: AccountListState,
) {
    val scrollBehavior = ToolbarBehavior.behavior()

    // One section title per section, and one Card per group of rows.
    val groups = remember(state.items) {
        val result = mutableListOf<AccountListGroup>()
        var card: MutableList<AccountItem.Item>? = null
        state.items.forEach { item ->
            when (item) {
                is AccountItem.Section -> {
                    card = null
                    result.add(AccountListGroup.Section(item.id, item.text))
                }

                is AccountItem.Item -> {
                    val current = card
                    if (current == null) {
                        val next = mutableListOf(item)
                        card = next
                        result.add(AccountListGroup.Card(next))
                    } else {
                        current.add(item)
                    }
                }
            }
        }
        result
    }

    ScaffoldLazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            LargeToolbar(
                title = stringResource(Res.string.account_main_header_title),
                navigationIcon = {
                    NavigationIcon()
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionState = run {
            val fabOnClick = state.onAddNewAccount
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
                    Icon(Icons.Outlined.Add, null)
                },
                text = {
                    Text(stringResource(Res.string.account_main_add_account_title))
                },
            )
        },
        bottomBar = {
            AccountsSelection(
                selection = state.selection,
            )
        },
    ) {
        if (state.items.isEmpty()) {
            item("header") {
                if (state.isLoading) {
                    SkeletonItem()
                } else {
                    EmptyView(
                        icon = {
                            Icon(Icons.Outlined.NoAccounts, null)
                        },
                        text = {
                            Text(
                                text = stringResource(Res.string.accounts_empty_label),
                            )
                        },
                    )
                }
            }
        }
        items(
            items = groups,
            key = { it.id },
        ) { group ->
            when (group) {
                is AccountListGroup.Section -> {
                    val text = group.text
                    if (text != null) {
                        SmallTitle(
                            text = text,
                        )
                    }
                }

                is AccountListGroup.Card -> {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 12.dp),
                    ) {
                        group.items.forEach { item ->
                            AccountListItem(
                                item = item,
                            )
                        }
                    }
                }
            }
        }
    }
}

private sealed interface AccountListGroup {
    val id: String

    data class Section(
        override val id: String,
        val text: String?,
    ) : AccountListGroup

    data class Card(
        val items: List<AccountItem.Item>,
    ) : AccountListGroup {
        override val id: String get() = "card:${items.first().id}"
    }
}
