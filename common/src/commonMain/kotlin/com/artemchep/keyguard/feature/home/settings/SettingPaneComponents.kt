package com.artemchep.keyguard.feature.home.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import arrow.core.partially1
import com.artemchep.keyguard.feature.home.vault.component.FlatDropdownSimpleExpressive
import com.artemchep.keyguard.feature.home.vault.component.FlatItemSimpleExpressive
import com.artemchep.keyguard.feature.home.vault.component.rememberFlatSurfaceExpressiveColor
import com.artemchep.keyguard.feature.home.vault.component.surfaceShape
import com.artemchep.keyguard.ui.AnimatedNewCounterBadge
import com.artemchep.keyguard.ui.ContextItem
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.feature.localization.textResource
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.KeyguardDropdownMenu
import com.artemchep.keyguard.ui.SwitchExpressive
import com.artemchep.keyguard.ui.icons.IconBox
import com.artemchep.keyguard.ui.icons.icon
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.LocalExpressive
import top.yukonga.miuix.kmp.basic.Badge
import top.yukonga.miuix.kmp.basic.BadgedBox
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role

@Stable
interface SettingPaneComponents {
    @Composable
    fun KgAction(
        icon: ImageVector?,
        subIcon: ImageVector? = null,
        badge: String? = null,
        title: @Composable RowScope.() -> Unit,
        text: (@Composable RowScope.() -> Unit)? = null,
        contentColor: Color = Color.Unspecified,
        trailing: (@Composable RowScope.() -> Unit)? = null,
        footer: (@Composable ColumnScope.() -> Unit)? = null,
        onClick: (() -> Unit)? = null,
        enabled: Boolean = onClick != null,
    )

    @Composable
    fun KgPicker(
        icon: ImageVector? = null,
        subIcon: ImageVector? = null,
        title: @Composable RowScope.() -> Unit,
        titleText: String,
        text: (@Composable RowScope.() -> Unit)? = null,
        trailing: (@Composable RowScope.() -> Unit)? = null,
        footer: (@Composable ColumnScope.() -> Unit)? = null,
        dropdown: List<ContextItem>,
    )

    @Composable
    fun KgSwitch(
        icon: ImageVector? = null,
        subIcon: ImageVector? = null,
        title: @Composable RowScope.() -> Unit,
        text: (@Composable RowScope.() -> Unit)? = null,
        footer: (@Composable ColumnScope.() -> Unit)? = null,
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
    )

    @Composable
    fun KgBlock(
        content: @Composable ColumnScope.() -> Unit,
    )
}

object SettingPaneComponentsDefault : SettingPaneComponents {
    @Composable
    override fun KgAction(
        icon: ImageVector?,
        subIcon: ImageVector?,
        badge: String?,
        title: @Composable (RowScope.() -> Unit),
        text: @Composable (RowScope.() -> Unit)?,
        contentColor: Color,
        trailing: @Composable (RowScope.() -> Unit)?,
        footer: @Composable (ColumnScope.() -> Unit)?,
        onClick: (() -> Unit)?,
        enabled: Boolean,
    ) {
        BasicComponent(
            startAction = if (icon != null) {
                // composable
                {
                    val content = remember(icon, subIcon) {
                        movableContentOf {
                            IconBox(
                                main = icon,
                                secondary = subIcon,
                            )
                        }
                    }

                    if (badge != null) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MiuixTheme.colorScheme.tertiaryContainer,
                                ) {
                                    Text(
                                        text = badge,
                                    )
                                }
                            },
                        ) {
                            content()
                        }
                    } else {
                        content()
                    }
                }
            } else {
                null
            },
            endActions = trailing,
            bottomAction = footer?.let { f ->
                {
                    Column {
                        f()
                    }
                }
            },
            insideMargin = BasicComponentDefaults.InsideMargin,
            onClick = onClick,
            enabled = enabled,
        ) {
            Column {
                Row {
                    title()
                }
                if (text != null) {
                    Row {
                        text()
                    }
                }
            }
        }
    }

    @Composable
    override fun KgPicker(
        icon: ImageVector?,
        subIcon: ImageVector?,
        title: @Composable (RowScope.() -> Unit),
        titleText: String,
        text: @Composable (RowScope.() -> Unit)?,
        trailing: @Composable (RowScope.() -> Unit)?,
        footer: @Composable (ColumnScope.() -> Unit)?,
        dropdown: List<ContextItem>,
    ) {
        var dropdownExpanded by remember {
            mutableStateOf(false)
        }
        val hapticFeedback = LocalHapticFeedback.current
        Box {
            BasicComponent(
                startAction = if (icon != null) {
                    icon(
                        main = icon,
                        secondary = subIcon,
                    )
                } else {
                    null
                },
                endActions = trailing,
                bottomAction = footer?.let { f ->
                    {
                        Column {
                            f()
                        }
                    }
                },
                insideMargin = BasicComponentDefaults.InsideMargin,
                role = Role.DropdownList,
                holdDownState = dropdownExpanded,
                onClick = {
                    dropdownExpanded = true
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                },
            ) {
                Column {
                    Row {
                        title()
                    }
                    if (text != null) {
                        Row {
                            text()
                        }
                    }
                }
            }
            KeyguardDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = {
                    dropdownExpanded = false
                },
            ) {
                dropdown.forEach { action ->
                    DropdownMenuItemFlat(action = action)
                }
            }
        }
    }

    @Composable
    override fun KgSwitch(
        icon: ImageVector?,
        subIcon: ImageVector?,
        title: @Composable (RowScope.() -> Unit),
        text: @Composable (RowScope.() -> Unit)?,
        footer: @Composable (ColumnScope.() -> Unit)?,
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
    ) {
        BasicComponent(
            startAction = if (icon != null) {
                icon(
                    main = icon,
                    secondary = subIcon,
                )
            } else {
                null
            },
            endActions = {
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                ) {
                    SwitchExpressive(
                        checked = checked,
                        enabled = onCheckedChange != null,
                        onCheckedChange = onCheckedChange,
                    )
                }
            },
            bottomAction = footer?.let { f ->
                {
                    Column {
                        f()
                    }
                }
            },
            insideMargin = BasicComponentDefaults.InsideMargin,
            onClick = onCheckedChange?.let { change ->
                {
                    change(!checked)
                }
            },
        ) {
            Column {
                Row {
                    title()
                }
                if (text != null) {
                    Row {
                        text()
                    }
                }
            }
        }
    }

    @Composable
    override fun KgBlock(
        content: @Composable (ColumnScope.() -> Unit),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            content()
        }
    }
}

val LocalSettingPaneComponents = staticCompositionLocalOf<SettingPaneComponents> {
    SettingPaneComponentsDefault
}

@Composable
fun SettingPaneComponents.KgAction(
    icon: ImageVector?,
    subIcon: ImageVector? = null,
    title: String,
    text: String? = null,
    contentColor: Color = Color.Unspecified,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = onClick != null,
) {
    ArrowPreference(
        title = title,
        summary = text,
        startAction = if (icon != null) {
            icon(
                main = icon,
                secondary = subIcon,
            )
        } else {
            null
        },
        endActions = trailing ?: {},
        bottomAction = footer?.let { f ->
            {
                Column {
                    f()
                }
            }
        },
        onClick = onClick,
        enabled = enabled,
    )
}

@Composable
fun SettingPaneComponents.KgSwitch(
    icon: ImageVector? = null,
    subIcon: ImageVector? = null,
    title: String,
    text: String? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    SwitchPreference(
        title = title,
        summary = text,
        startAction = if (icon != null) {
            icon(
                main = icon,
                secondary = subIcon,
            )
        } else {
            null
        },
        bottomAction = footer?.let { f ->
            {
                Column {
                    f()
                }
            }
        },
        checked = checked,
        onCheckedChange = onCheckedChange ?: {},
        enabled = onCheckedChange != null,
    )
}

@Composable
fun SettingPaneComponents.KgPicker(
    icon: ImageVector? = null,
    subIcon: ImageVector? = null,
    title: String,
    titleText: String = title,
    text: String? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    dropdown: List<ContextItem>,
) {
    val actions = dropdown.filterIsInstance<FlatItemAction>()
    OverlayDropdownPreference(
        title = title,
        summary = text,
        startAction = if (icon != null) {
            icon(
                main = icon,
                secondary = subIcon,
            )
        } else {
            null
        },
        items = actions.map { action -> textResource(action.title) },
        selectedIndex = actions.indexOfFirst { it.selected },
        onSelectedIndexChange = { index ->
            actions.getOrNull(index)?.onClick?.invoke()
        },
    )
}
