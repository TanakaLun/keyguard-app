package com.artemchep.keyguard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.artemchep.keyguard.ui.surface.LocalSurfaceColor
import top.yukonga.miuix.kmp.overlay.OverlayListPopup
import top.yukonga.miuix.kmp.theme.MiuixTheme

val DropdownMinWidth = 256.dp

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
        popupModifier = modifier.widthIn(min = DropdownMinWidth),
        minWidth = DropdownMinWidth,
        enableWindowDim = false,
        onDismissRequest = onDismissRequest,
    ) {
        AccessibilityDataProtectionEffect()
        Column {
            val scope = DropdownScopeImpl(
                parent = this,
                onDismissRequest = onDismissRequest,
            )
            CompositionLocalProvider(
                LocalSurfaceColor provides MiuixTheme.colorScheme.surfaceContainer,
            ) {
                content(scope)
            }
        }
    }
}