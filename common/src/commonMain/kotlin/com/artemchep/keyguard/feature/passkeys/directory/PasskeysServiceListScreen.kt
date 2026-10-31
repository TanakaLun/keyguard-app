package com.artemchep.keyguard.feature.passkeys.directory

import androidx.compose.foundation.background
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import com.artemchep.keyguard.URL_PASSKEYS
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.feature.EmptySearchView
import top.yukonga.miuix.kmp.basic.BasicComponent
import com.artemchep.keyguard.feature.localization.textResource
import com.artemchep.keyguard.feature.navigation.navigationNextEntryOrNull
import com.artemchep.keyguard.feature.servicedirectory.ServiceDirectoryListScaffold
import com.artemchep.keyguard.feature.twopane.LocalHasDetailPane
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.passkeys_directory_search_placeholder
import com.artemchep.keyguard.res.passkeys_directory_title
import com.artemchep.keyguard.ui.theme.selectedContainer
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun PasskeysListScreen(
) {
    val loadableState = producePasskeysListState()
    PasskeysListScreen(
        loadableState = loadableState,
    )
}

@Composable
fun PasskeysListScreen(
    loadableState: Loadable<PasskeysServiceListState>,
) {
    ServiceDirectoryListScaffold(
        loadableState = loadableState,
        title = stringResource(Res.string.passkeys_directory_title),
        url = URL_PASSKEYS,
        searchPlaceholder = stringResource(Res.string.passkeys_directory_search_placeholder),
        errorText = "Failed to load app list!",
        filter = PasskeysServiceListState::filter,
        content = PasskeysServiceListState::content,
        filterRevision = PasskeysServiceListState.Filter::revision,
        filterQuery = PasskeysServiceListState.Filter::query,
        contentRevision = PasskeysServiceListState.Content::revision,
        contentItems = PasskeysServiceListState.Content::items,
        itemKey = { it.key },
        sectionNameOrNull = { item ->
            (item as? PasskeysServiceListState.Item.Section)?.name
        },
        contentItemOrNull = { item ->
            item as? PasskeysServiceListState.Item.Content
        },
        noItems = { modifier ->
            NoItemsPlaceholder(
                modifier = modifier,
            )
        },
        contentItem = { modifier, item ->
            AppItem(
                modifier = modifier,
                item = item,
            )
        },
    )
}

@Composable
private fun NoItemsPlaceholder(
    modifier: Modifier = Modifier,
) {
    EmptySearchView(
        modifier = modifier,
    )
}

@Composable
private fun AppItem(
    modifier: Modifier,
    item: PasskeysServiceListState.Item.Content,
) {
    val backgroundColor = run {
        if (LocalHasDetailPane.current) {
            val nextEntry = navigationNextEntryOrNull()
            val nextRoute = nextEntry?.route as? PasskeysServiceViewFullRoute

            val selected = nextRoute?.args?.model?.id == item.data.id
            if (selected) {
                return@run MiuixTheme.colorScheme.selectedContainer
            }
        }

        Color.Unspecified
    }
    BasicComponent(
        modifier = modifier
            .then(
                if (backgroundColor.isSpecified) {
                    Modifier.background(backgroundColor)
                } else {
                    Modifier
                },
            ),
        title = textResource(item.name),
        startAction = {
            item.icon()
        },
        onClick = item.onClick,
    )
}
