package com.artemchep.keyguard.feature.gpgagent.keyserver.search

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.artemchep.keyguard.common.model.GpgKeyserverConfig
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.EmptySearchView
import com.artemchep.keyguard.feature.qr.ScanQrButton
import com.artemchep.keyguard.feature.servicedirectory.ServiceDirectoryListScaffold
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.icons.IconBox
import com.artemchep.keyguard.ui.theme.combineAlpha
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.labelMedium

@Composable
fun GpgKeyserverSearchScreen() {
    val loadableState = produceGpgKeyserverSearchState()
    GpgKeyserverSearchScreen(
        loadableState = loadableState,
    )
}

@Composable
fun GpgKeyserverSearchScreen(
    loadableState: Loadable<GpgKeyserverSearchState>,
) {
    val state = loadableState.getOrNull()
    val filter = state?.filter?.collectAsState()
    val query = filter?.value?.query?.text.orEmpty()

    ServiceDirectoryListScaffold(
        loadableState = loadableState,
        title = stringResource(Res.string.gpg_keyserver_search_header_title),
        url = state?.keyserverUrl ?: GpgKeyserverConfig.DEFAULT_URL,
        searchPlaceholder = stringResource(Res.string.gpg_keyserver_search_placeholder),
        errorText = stringResource(Res.string.gpg_keyserver_search_error_text),
        filter = GpgKeyserverSearchState::filter,
        content = GpgKeyserverSearchState::content,
        filterRevision = GpgKeyserverSearchState.Filter::revision,
        filterQuery = GpgKeyserverSearchState.Filter::query,
        contentRevision = GpgKeyserverSearchState.Content::revision,
        contentItems = GpgKeyserverSearchState.Content::items,
        itemKey = { it.key },
        sectionNameOrNull = { null },
        contentItemOrNull = { item ->
            item as? GpgKeyserverSearchState.Item.Content
        },
        noItems = { modifier ->
            NoItemsPlaceholder(
                modifier = modifier,
                hasQuery = query.isNotBlank(),
            )
        },
        contentItem = { modifier, item ->
            KeyItem(
                modifier = modifier,
                item = item,
            )
        },
        searchTrailing = {
            ScanQrButton(
                onValueChange = state?.onScanned,
            )
        },
    )
}

@Composable
private fun NoItemsPlaceholder(
    modifier: Modifier = Modifier,
    hasQuery: Boolean,
) {
    EmptySearchView(
        modifier = modifier,
        text = {
            Text(
                text = stringResource(
                    if (hasQuery) {
                        Res.string.gpg_keyserver_search_empty_label
                    } else {
                        Res.string.gpg_keyserver_search_empty_query_label
                    },
                ),
            )
        },
    )
}

@Composable
private fun KeyItem(
    modifier: Modifier,
    item: GpgKeyserverSearchState.Item.Content,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val onClick: (() -> Unit)? = when {
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
            modifier = modifier,
            onClick = onClick,
            startAction = {
                IconBox(Icons.Outlined.Key)
            },
            content = {
                FlatItemTextContent(
                    title = {
                        Text(
                            text = item.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    text = {
                        Text(
                            text = item.description,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                )
            },
            endActions = {
                if (item.result.revoked) {
                    Text(
                        color = MiuixTheme.colorScheme.error,
                        style = MiuixTheme.textStyles.labelMedium,
                        text = stringResource(Res.string.gpg_keyserver_search_revoked_label),
                    )
                } else if (item.result.publicKeyArmored == null) {
                    Text(
                        color = LocalContentColor.current.combineAlpha(0.64f),
                        style = MiuixTheme.textStyles.labelMedium,
                        text = stringResource(Res.string.gpg_keyserver_search_index_label),
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
