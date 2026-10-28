package com.artemchep.keyguard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.DropdownDefaults
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.Check
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.vault.component.Section
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun DropdownScope.DropdownMenuItemFlat(
    action: ContextItem,
) {
    when (action) {
        is ContextItem.Section -> {
            Section(
                modifier = Modifier
                    .fillMaxWidth(),
                text = action.title,
            )
        }

        is ContextItem.Custom -> {
            Column(
                modifier = Modifier
                    .widthIn(max = DropdownMinWidth),
            ) {
                action.content()
            }
        }

        is FlatItemAction -> {
            DropdownMenuItemFlat(action)
        }
    }
}

@Composable
fun DropdownScope.DropdownMenuItemFlat(
    action: FlatItemAction,
) {
    // Mirrors the Miuix DropdownImpl row: selectable row with the standard
    // paddings, colors and a trailing check icon on the selected option.
    val colors = DropdownDefaults.dropdownColors()
    val enabled = action.onClick != null
    val hapticFeedback = LocalHapticFeedback.current
    val backgroundColor = if (action.selected) {
        colors.selectedContainerColor
    } else {
        Color.Transparent
    }
    val contentColor = when {
        !enabled -> MiuixTheme.colorScheme.disabledOnSecondaryVariant
        action.danger -> MiuixTheme.colorScheme.error
        action.selected -> colors.selectedContentColor
        else -> colors.contentColor
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(backgroundColor)
            }
            .selectable(
                selected = action.selected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                    action.onClick?.invoke()
                    onDismissRequest()
                },
            )
            .padding(
                horizontal = DropdownDefaults.InsideHorizontalPadding,
                vertical = DropdownDefaults.MiddleVerticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlatItemActionContent(
                    action = action,
                    compact = true,
                )
            }
        }
        if (action.selected) {
            Image(
                modifier = Modifier
                    .padding(start = DropdownDefaults.CheckIconStartPadding)
                    .size(DropdownDefaults.CheckIconSize),
                imageVector = MiuixIcons.Basic.Check,
                colorFilter = ColorFilter.tint(colors.selectedIndicatorColor),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun DropdownScope.DropdownMenuItemFlatLayout(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .widthIn(max = DropdownMinWidth)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun OptionsButton(
    actions: List<ContextItem> = emptyList(),
) {
    if (actions.isNotEmpty()) {
        OptionsButton(
            enabled = actions.isNotEmpty(),
        ) {
            actions.forEachIndexed { index, action ->
                DropdownMenuItemFlat(
                    action = action,
                )
            }
        }
    }
}

@Composable
fun OptionsButton(
    enabled: Boolean = true,
    icon: ImageVector = Icons.Outlined.MoreVert,
    contentDescription: String? = null,
    content: @Composable DropdownScope.() -> Unit,
) {
    var isAutofillWindowShowing by remember {
        mutableStateOf(false)
    }

    if (!enabled) {
        // We can not autofill disabled text fields and we can not
        // tease user with it.
        isAutofillWindowShowing = false
    }

    Box {
        val hapticFeedback = LocalHapticFeedback.current
        IconButton(
            enabled = enabled,
            holdDownState = isAutofillWindowShowing,
            onClick = {
                isAutofillWindowShowing = !isAutofillWindowShowing
                if (isAutofillWindowShowing) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                }
            },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }
        // Inject the dropdown popup to the bottom of the
        // content.
        val onDismissRequest = {
            isAutofillWindowShowing = false
        }
        KeyguardDropdownMenu(
            expanded = isAutofillWindowShowing,
            onDismissRequest = onDismissRequest,
        ) {
            content(this)
        }
    }
}
