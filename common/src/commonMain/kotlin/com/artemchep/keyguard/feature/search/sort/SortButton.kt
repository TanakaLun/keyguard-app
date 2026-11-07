package com.artemchep.keyguard.feature.search.sort

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.localization.textResource
import com.artemchep.keyguard.feature.search.EmptyItem
import com.artemchep.keyguard.feature.search.component.DropdownButton
import com.artemchep.keyguard.feature.search.sort.model.SortItemModel
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.ContextItem
import com.artemchep.keyguard.ui.DropdownMenuItemFlat
import com.artemchep.keyguard.ui.DropdownScope
import com.artemchep.keyguard.ui.FlatItemAction
import org.jetbrains.compose.resources.stringResource

@Composable
fun SortButton(
    modifier: Modifier = Modifier,
    items: List<SortItemModel>,
    onClear: (() -> Unit)?,
) {
    DropdownButton(
        modifier = modifier,
        icon = Icons.Outlined.SortByAlpha,
        title = stringResource(Res.string.sort_header_title),
        items = items,
        onClear = onClear,
    ) {
        SortItems(
            items = items,
        )
    }
}

@Composable
private fun DropdownScope.SortItems(
    items: List<SortItemModel>,
) {
    if (items.isEmpty()) {
        EmptyItem(
            text = stringResource(Res.string.sort_empty_label),
        )
        return
    }

    items.forEach { item ->
        key(item.id) {
            when (item) {
                is SortItemModel.Item -> DropdownMenuItemFlat(
                    action = FlatItemAction(
                        id = item.id,
                        icon = item.icon,
                        title = item.title,
                        selected = item.checked,
                        onClick = item.onClick,
                    ),
                )

                is SortItemModel.Section -> DropdownMenuItemFlat(
                    action = ContextItem.Section(
                        title = item.text?.let { textResource(it) },
                    ),
                )
            }
        }
    }
}
