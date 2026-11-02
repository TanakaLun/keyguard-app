package com.artemchep.keyguard.ui

import kotlin.jvm.JvmName

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import top.yukonga.miuix.kmp.theme.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.andThen
import com.artemchep.keyguard.common.usecase.CopyText
import com.artemchep.keyguard.feature.home.vault.component.VaultItemIcon2
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevationSemi
import com.artemchep.keyguard.feature.home.vault.model.VaultItemIcon
import com.artemchep.keyguard.feature.localization.TextHolder
import com.artemchep.keyguard.feature.localization.textResource
import com.artemchep.keyguard.feature.navigation.keyboard.KeyShortcut
import com.artemchep.keyguard.platform.CurrentPlatform
import com.artemchep.keyguard.platform.Platform
import com.artemchep.keyguard.ui.theme.combineAlpha
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.KgShapes
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.bodyMedium
import com.artemchep.keyguard.ui.theme.bodySmall
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevationSemi
import com.artemchep.keyguard.ui.theme.surfaceColorAtElevation

private inline val flatItemSmallCornerSizeDp get() = 4.dp

private val flatItemSmallCornerSize = CornerSize(flatItemSmallCornerSizeDp)

private val flatItemSmallShape = RoundedCornerShape(flatItemSmallCornerSizeDp)

sealed interface ContextItem {
    data class Section(
        val title: String? = null,
    ) : ContextItem

    data class Custom(
        val content: @Composable () -> Unit,
    ) : ContextItem
}

data class FlatItemAction(
    val id: String? = null,
    val icon: ImageVector? = null,
    val leading: (@Composable () -> Unit)? = null,
    val trailing: (@Composable () -> Unit)? = null,
    val title: TextHolder,
    val text: TextHolder? = null,
    val type: Type? = null,
    val shortcut: KeyShortcut? = null,
    val selected: Boolean = false,
    val danger: Boolean = false,
    val onClick: (() -> Unit)? = {},
) : ContextItem {
    companion object;

    enum class Type {
        COPY,
        DOWNLOAD,
        VIEW,
    }
}

inline fun buildContextItems(
    vararg items: List<ContextItem>,
    block: ContextItemBuilder.() -> Unit,
) = ContextItemBuilder(items = items).apply(block).build()

class ContextItemBuilder(
    vararg items: List<ContextItem>,
) {
    private val items = kotlin.run {
        val out = mutableListOf<ContextItem>()
        items.forEach { list ->
            if (list.isEmpty()) {
                return@forEach
            }

            val header = list.first()
            if (header !is ContextItem.Section) {
                out += ContextItem.Section()
            }

            out += list
        }
        out
    }

    inline fun section(
        title: String? = null,
        block: ContextItemBuilder.() -> Unit,
    ) {
        val sectionItems = buildContextItems(block = block)
        if (sectionItems.isEmpty()) {
            return
        }

        this += ContextItem.Section(title = title)
        this += sectionItems
    }

    operator fun plusAssign(item: ContextItem?) {
        items += item
            ?: return
    }

    operator fun plusAssign(item: Collection<ContextItem>) {
        items += item
    }

    fun build(): PersistentList<ContextItem> = sequence<ContextItem> {
        items.forEachIndexed { index, item ->
            // Skip a first item if it has no title.
            if (
                index == 0 &&
                item is ContextItem.Section &&
                item.title == null
            ) {
                return@forEachIndexed
            }

            yield(item)
        }
    }.toPersistentList()
}


@JvmName("FlatItemActionNullable")
fun CopyText.FlatItemAction(
    title: TextHolder,
    value: String?,
    shortcut: KeyShortcut? = null,
    hidden: Boolean = false,
    type: CopyText.Type = CopyText.Type.VALUE,
    id: String? = null,
) = value?.let {
    FlatItemAction(
        title = title,
        value = it,
        shortcut = shortcut,
        hidden = hidden,
        type = type,
        id = id,
    )
}

fun CopyText.FlatItemAction(
    leading: (@Composable () -> Unit)? = null,
    title: TextHolder,
    value: String,
    shortcut: KeyShortcut? = null,
    hidden: Boolean = false,
    type: CopyText.Type = CopyText.Type.VALUE,
    id: String? = null,
) = FlatItemAction(
    id = id,
    leading = leading,
    icon = Icons.Outlined.ContentCopy,
    title = title,
    text = value.takeUnless { hidden }
        ?.let(TextHolder::Value),
    shortcut = shortcut,
    type = FlatItemAction.Type.COPY,
    onClick = {
        copy(
            text = value,
            hidden = hidden,
            type = type,
        )
    },
)

interface DropdownScope : ColumnScope {
    val onDismissRequest: () -> Unit
}

fun DropdownScope.dismissOnClick(block: () -> Unit) = block.andThen { onDismissRequest() }

class DropdownScopeImpl(
    parent: ColumnScope,
    override val onDismissRequest: () -> Unit,
) : DropdownScope, ColumnScope by parent

@Composable
fun <T> ColumnScope.DropdownMenuExpandableContainer(
    dropdownScope: DropdownScope,
    list: List<T>,
    maxItems: Int = 4,
    render: @Composable (T) -> Unit,
) {
    var maximized by remember {
        mutableStateOf(false)
    }
    list.forEachIndexed { i, el ->
        if (!maximized) {
            if (i == maxItems) {
                dropdownScope.DropdownMenuItemFlatLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                        )
                        .clip(KgShapes.medium)
                        .clickable {
                            maximized = true
                        },
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(18.dp),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            }
            if (i >= maxItems) {
                return@forEachIndexed
            }
        }
        render(el)
    }
}

val DefaultEmphasisAlpha = 1f
val HighEmphasisAlpha = 0.87f
val MediumEmphasisAlpha = 0.6f
val DisabledEmphasisAlpha = 0.38f

@Composable
fun RowScope.FlatItemActionContent(
    action: FlatItemAction,
    compact: Boolean = false,
) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current
            .let { color ->
                if (action.onClick != null) {
                    color
                } else {
                    color.combineAlpha(DisabledEmphasisAlpha)
                }
            },
    ) {
        if (action.icon != null || action.leading != null) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(16.dp),
            ) {
                if (action.icon != null) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null,
                    )
                }
                action.leading?.invoke()
            }
            Spacer(
                modifier = Modifier
                    .width(8.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = textResource(action.title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MiuixTheme.textStyles.bodyLarge,
            )
            if (action.text != null) {
                Text(
                    text = textResource(action.text),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    style = if (compact) {
                        MiuixTheme.textStyles.bodySmall
                    } else {
                        MiuixTheme.textStyles.bodyMedium
                    },
                    color = LocalContentColor.current
                        .combineAlpha(MediumEmphasisAlpha),
                )
            }
        }
        if (action.trailing != null) {
            Spacer(Modifier.width(8.dp))
            action.trailing.invoke()
        }
    }
}

@Composable
fun defaultAvatarColor() = LocalContentColor.current
    .copy(alpha = 0.05f * LocalContentColor.current.alpha)

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    color: Color = defaultAvatarColor(),
    shape: Shape = KgShapes.medium,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(shape)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvatarBuilder(
    modifier: Modifier = Modifier,
    icon: VaultItemIcon,
    accent: Color = Color.Unspecified,
    active: Boolean,
    badge: @Composable () -> Unit,
) = Box(
    modifier = modifier,
) {
    val avatarColor = accent
        .takeIf { color ->
            val fits = icon is VaultItemIcon.VectorIcon ||
                    icon is VaultItemIcon.TextIcon
            fits && active && color.isSpecified
        }
        ?.let { color ->
            MiuixTheme.colorScheme.background
                .combineAlpha(0.25f)
                .compositeOver(
                    background = color,
                )
        }
        ?: MiuixTheme.colorScheme
            .surfaceColorAtElevationSemi(
                elevation = LocalAbsoluteTonalElevation.current + 8.dp,
            )
    Avatar(
        color = avatarColor,
    ) {
        VaultItemIcon2(
            icon = icon,
        )
    }

    val backgroundColor = if (!active) {
        MiuixTheme.colorScheme.surfaceVariant
    } else {
        MiuixTheme.colorScheme.tertiaryContainer
    }
    AvatarBadgeSurface(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .widthIn(max = 36.dp)
            .wrapContentWidth(
                align = Alignment.Start,
                unbounded = true,
            ),
        backgroundColor = backgroundColor,
        content = badge,
    )
}

@Composable
fun AvatarBadgeSurface(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    val contentColor = contentColorFor(backgroundColor)
    FlowRow(
        modifier = modifier
            .clip(KgShapes.extraSmall)
            .background(backgroundColor),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
        ) {
            content()
        }
    }
}

@Composable
fun AvatarBadgeIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
) {
    Icon(
        modifier = modifier
            .size(16.dp)
            .padding(1.dp),
        imageVector = imageVector,
        contentDescription = null,
    )
}
