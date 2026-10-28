package com.artemchep.keyguard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.overlay.OverlayListPopup

val DropdownMinWidth = 256.dp

/**
 * A standard Miuix dropdown: an overlay list popup anchored to the caller and
 * rendered through the root scaffold host.
 */
@Composable
fun KeyguardDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    @Suppress("UNUSED_PARAMETER")
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable DropdownScope.() -> Unit,
) {
    OverlayListPopup(
        show = expanded,
        popupModifier = modifier,
        alignment = PopupPositionProvider.Align.End,
        onDismissRequest = onDismissRequest,
    ) {
        AccessibilityDataProtectionEffect()
        ListPopupColumn {
            Column {
                val scope = DropdownScopeImpl(
                    parent = this,
                    onDismissRequest = onDismissRequest,
                )
                content(scope)
            }
        }
    }
}
