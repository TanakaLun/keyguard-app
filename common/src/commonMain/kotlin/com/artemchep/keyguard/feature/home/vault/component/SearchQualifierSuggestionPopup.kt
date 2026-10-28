package com.artemchep.keyguard.feature.home.vault.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import top.yukonga.miuix.kmp.overlay.OverlayListPopup
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchQualifierSuggestionPopup(
    suggestion: String,
    onClick: () -> Unit,
) {
    OverlayListPopup(
        show = true,
        onDismissRequest = {},
    ) {
        ListPopupColumn {
            SmartBadge(
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 1.dp,
                    ),
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                    )
                },
                title = suggestion,
                text = "Tab",
                onClick = onClick,
            )
        }
    }
}
