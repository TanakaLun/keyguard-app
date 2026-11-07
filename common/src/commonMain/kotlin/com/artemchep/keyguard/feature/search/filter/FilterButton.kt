package com.artemchep.keyguard.feature.search.filter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.localization.TextHolder
import com.artemchep.keyguard.feature.search.EmptyItem
import com.artemchep.keyguard.feature.search.component.DropdownButton
import com.artemchep.keyguard.feature.search.filter.component.VaultHomeScreenFilterPaneNumberOfItems
import com.artemchep.keyguard.feature.search.filter.model.FilterItemModel
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.ContextItem
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.DropdownScope
import com.artemchep.keyguard.ui.FlatItemAction
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    count: Int?,
    items: List<FilterItemModel>,
    onClear: (() -> Unit)?,
    onSave: (() -> Unit)?,
) {
    DropdownButton(
        modifier = modifier,
        icon = Icons.Outlined.FilterAlt,
        title = stringResource(Res.string.filter_header_title),
        items = items,
        onClear = onClear,
        onSave = onSave,
    ) {
        if (count != null) {
            DropdownMenuItemFlat(
                action = ContextItem.Custom {
                    VaultHomeScreenFilterPaneNumberOfItems(
                        count = count,
                    )
                },
            )
        }

        FilterDropdownItems(
            items = items,
            predicate = {
                !it.id.startsWith("custom")
            },
        )
    }
}

/**
 * The filters as standard Miuix dropdown rows.
 */
@Composable
private fun DropdownScope.FilterDropdownItems(
    items: List<FilterItemModel>,
    predicate: (FilterItemModel) -> Boolean = { true },
) {
    val visibleItems = items
        .filter(predicate)
    if (visibleItems.isEmpty()) {
        EmptyItem(
            text = stringResource(Res.string.filter_empty_label),
        )
        return
    }

    visibleItems.forEach { item ->
        key(item.id) {
            when (item) {
                is FilterItemModel.Section -> DropdownMenuItemFlat(
                    action = ContextItem.Section(
                        title = item.text,
                    ),
                )

                is FilterItemModel.ChipItem -> DropdownMenuItemFlat(
                    action = FlatItemAction(
                        id = item.id,
                        leading = item.leading,
                        title = TextHolder.Value(item.title),
                        text = item.text?.let { TextHolder.Value(it) },
                        selected = item.checked,
                        onClick = item.onClick,
                    ),
                )

                is FilterItemModel.ListItem -> DropdownMenuItemFlat(
                    action = FlatItemAction(
                        id = item.id,
                        leading = item.leading,
                        title = TextHolder.Value(item.title),
                        text = item.text?.let { TextHolder.Value(it) },
                        selected = item.checked,
                        onClick = item.onClick,
                    ),
                )
            }
        }
    }
}
