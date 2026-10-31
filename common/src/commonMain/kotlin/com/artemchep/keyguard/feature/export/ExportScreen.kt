package com.artemchep.keyguard.feature.export

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Warning
import top.yukonga.miuix.kmp.basic.Badge
import top.yukonga.miuix.kmp.basic.BadgedBox
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.material3.TopAppBarDefaults
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.common.service.permission.PermissionState
import com.artemchep.keyguard.feature.home.vault.component.FlatItemLayoutExpressive
import top.yukonga.miuix.kmp.basic.BasicComponent
import com.artemchep.keyguard.feature.home.vault.model.FilterItem
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.feature.search.filter.FilterButton
import com.artemchep.keyguard.feature.search.filter.FilterScreen
import com.artemchep.keyguard.feature.twopane.TwoPaneScreen
import com.artemchep.keyguard.platform.LocalLeContext
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.AutofillButton
import com.artemchep.keyguard.ui.DefaultFab
import com.artemchep.keyguard.ui.ExpandedIfNotEmpty
import com.artemchep.keyguard.ui.FabState
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.OptionsButton
import com.artemchep.keyguard.ui.PasswordFlatTextField
import com.artemchep.keyguard.ui.ScaffoldColumn
import com.artemchep.keyguard.ui.SwitchExpressive
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.icons.KeyguardAttachment
import com.artemchep.keyguard.ui.icons.KeyguardCipher
import com.artemchep.keyguard.ui.icons.IconBox
import com.artemchep.keyguard.ui.skeleton.SkeletonTextField
import com.artemchep.keyguard.ui.theme.badgeContainer
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.theme.onWarningContainer
import com.artemchep.keyguard.ui.theme.warning
import com.artemchep.keyguard.ui.theme.warningContainer
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.SmallToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import com.artemchep.keyguard.ui.util.HorizontalDivider
import org.jetbrains.compose.resources.stringResource
import kotlinx.collections.immutable.persistentListOf
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.bodyMedium

@Composable
fun ExportScreen(
    args: ExportRoute.Args,
) {
    val loadableState = produceExportScreenState(
        args = args,
    )

    val title = args.title
        ?: stringResource(Res.string.exportaccount_header_title)
    val scrollBehavior = ToolbarBehavior.behavior()
    when (loadableState) {
        is Loadable.Ok -> {
            val state = loadableState.value
            ExportScreenOk(
                title = title,
                scrollBehavior = scrollBehavior,
                state = state,
            )
        }

        is Loadable.Loading -> {
            ExportScreenSkeleton(
                title = title,
                scrollBehavior = scrollBehavior,
            )
        }
    }
}

@Composable
fun ExportScreenSkeleton(
    title: String,
    scrollBehavior: ScrollBehavior,
) {
    TwoPaneScreen(
        header = { modifier ->
            SmallToolbar(
                modifier = modifier,
                containerColor = Color.Transparent,
                title = title,
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
            val items = persistentListOf<FilterItem>()
            ExportScreenFilterList(
                modifier = modifier,
                items = items,
                onClear = null,
                onSave = null,
            )
        },
    ) { modifier, tabletUi ->
        ExportScreen(
            modifier = modifier,
            items = null,
            filter = null,
            password = null,
            content = null,
            loading = true,
            tabletUi = tabletUi,
            title = title,
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
fun ExportScreenOk(
    title: String,
    scrollBehavior: ScrollBehavior,
    state: ExportState,
) {
    val items by state.itemsFlow.collectAsState()
    val attachments by state.attachmentsFlow.collectAsState()
    val filter by state.filterFlow.collectAsState()
    val password by state.passwordFlow.collectAsState()
    val content by state.contentFlow.collectAsState()
    TwoPaneScreen(
        header = { modifier ->
            SmallToolbar(
                modifier = modifier,
                containerColor = Color.Transparent,
                title = title,
                navigationIcon = {
                    NavigationIcon()
                },
                actions = {
                    OptionsButton(
                        //actions = state.actions,
                    )
                },
            )

            SideEffect {
                if (scrollBehavior.state.heightOffsetLimit != 0f) {
                    scrollBehavior.state.heightOffsetLimit = 0f
                }
            }
        },
        detail = { modifier ->
            ExportScreenFilterList(
                modifier = modifier,
                items = filter.items,
                onClear = filter.onClear,
                onSave = filter.onSave,
            )
        },
    ) { modifier, tabletUi ->
        ExportScreen(
            modifier = modifier,
            items = items,
            attachments = attachments,
            filter = filter,
            password = password,
            content = content,
            loading = false,
            tabletUi = tabletUi,
            title = title,
            scrollBehavior = scrollBehavior,
        )
    }
}

//
// Filter
//

@Composable
private fun ExportScreenFilterList(
    modifier: Modifier = Modifier,
    items: List<FilterItem>,
    onClear: (() -> Unit)?,
    onSave: (() -> Unit)?,
) {
    FilterScreen(
        modifier = modifier,
        count = null,
        items = items,
        onClear = onClear,
        onSave = onSave,
    )
}

@Composable
private fun ExportScreenFilterButton(
    modifier: Modifier = Modifier,
    items: List<FilterItem>,
    onClear: (() -> Unit)?,
    onSave: (() -> Unit)?,
) {
    FilterButton(
        modifier = modifier,
        count = null,
        items = items,
        onClear = onClear,
        onSave = onSave,
    )
}

//
// Main
//

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExportScreen(
    modifier: Modifier,
    items: ExportState.Items? = null,
    attachments: ExportState.Attachments? = null,
    filter: ExportState.Filter? = null,
    password: ExportState.Password? = null,
    content: ExportState.Content? = null,
    loading: Boolean,
    tabletUi: Boolean,
    title: String,
    scrollBehavior: ScrollBehavior,
) {
    ScaffoldColumn(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        expressive = true,
        topAppBarScrollBehavior = scrollBehavior,
        topBar = {
            if (tabletUi) {
                return@ScaffoldColumn
            }

            LargeToolbar(
                title = title,
                navigationIcon = {
                    NavigationIcon()
                },
                actions = {
                    if (filter != null) {
                        ExportScreenFilterButton(
                            modifier = Modifier,
                            items = filter.items,
                            onClear = filter.onClear,
                            onSave = filter.onSave,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionState = run {
            val fabOnClick = content?.onExportClick
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
                    Icon(Icons.Outlined.SaveAlt, null)
                },
                text = {
                    Text(
                        text = stringResource(Res.string.exportaccount_export_button),
                    )
                },
            )
        },
    ) {
        // Add extra padding to match the horizontal and
        // vertical paddings.
        if (tabletUi) {
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
        }

        if (loading) {
            ExportContentSkeleton()
        } else if (
            items != null &&
            attachments != null &&
            password != null &&
            content != null
        ) {
            ExportContentOk(
                items = items,
                attachments = attachments,
                password = password,
                content = content,
            )
        }

        Spacer(
            modifier = Modifier
                .height(32.dp),
        )
        Icon(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = LocalContentColor.current.combineAlpha(alpha = MediumEmphasisAlpha),
        )
        Spacer(
            modifier = Modifier
                .height(16.dp),
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = stringResource(Res.string.exportaccount_format_note),
            style = MiuixTheme.textStyles.bodyMedium,
            color = LocalContentColor.current
                .combineAlpha(alpha = MediumEmphasisAlpha),
        )
        ExpandedIfNotEmpty(
            Unit.takeIf { attachments?.enabled == true },
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .height(16.dp),
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    text = stringResource(Res.string.exportaccount_attachments_note),
                    style = MiuixTheme.textStyles.bodyMedium,
                    color = LocalContentColor.current
                        .combineAlpha(alpha = MediumEmphasisAlpha),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.ExportContentSkeleton(
) {
    SkeletonTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
    )
}

@Composable
private fun ColumnScope.ExportContentOk(
    items: ExportState.Items,
    attachments: ExportState.Attachments,
    password: ExportState.Password,
    content: ExportState.Content,
) {
    ExpandedIfNotEmpty(
        valueOrNull = content.writePermission as? PermissionState.Declined,
    ) { permission ->
        val updatedContext by rememberUpdatedState(LocalLeContext)
        CompositionLocalProvider(
            LocalContentColor provides MiuixTheme.colorScheme.onWarningContainer,
        ) {
            BasicComponent(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp)
                    .background(MiuixTheme.colorScheme.warningContainer),
                title = stringResource(Res.string.pref_item_permission_write_external_storage_grant),
                startAction = {
                    IconBox(
                        main = Icons.Outlined.Storage,
                        secondary = Icons.Outlined.Warning,
                    )
                },
                onClick = {
                    permission.ask(updatedContext)
                },
            )
        }
    }
    PasswordFlatTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        label = stringResource(Res.string.exportaccount_password_label),
        value = password.model,
        trailing = {
            AutofillButton(
                key = "password",
                password = true,
                onValueChange = password.model.onSetText,
            )
        },
    )
    if (attachments.onToggle != null) {
        Card(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
        ) {
            FlatItemLayoutExpressive(
                leading = {
                    Icon(Icons.Outlined.KeyguardAttachment, null)
                },
                content = {
                    FlatItemTextContent(
                        title = {
                            Text(
                                text = stringResource(Res.string.exportaccount_include_attachments_title),
                            )
                        },
                        text = {
                            val size = attachments.size
                                ?: return@FlatItemTextContent
                            Text(size)
                        },
                    )
                },
                trailing = {
                    SwitchExpressive(
                        checked = attachments.enabled,
                        onCheckedChange = null,
                    )
                },
                onClick = attachments.onToggle,
            )
        }
    }

    val updateOnClickView by rememberUpdatedState(items.onView)
    Button(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        onClick = {
            updateOnClickView
                ?.invoke()
        },
        enabled = updateOnClickView != null,
    ) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MiuixTheme.colorScheme.badgeContainer,
                ) {
                    val size = items.count
                    Text(text = size.toString())
                }
            },
        ) {
            Icon(
                modifier = Modifier
                    .size(ButtonDefaults.IconSize),
                imageVector = Icons.Outlined.KeyguardCipher,
                contentDescription = null,
            )
        }
        Spacer(
            modifier = Modifier
                .width(24.dp),
        )
        Text(
            text = stringResource(Res.string.items),
        )
    }
}
