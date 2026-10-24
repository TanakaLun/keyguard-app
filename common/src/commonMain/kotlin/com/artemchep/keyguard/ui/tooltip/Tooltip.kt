package com.artemchep.keyguard.ui.tooltip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.basic.TooltipAnchorPosition
import top.yukonga.miuix.kmp.basic.TooltipBox
import top.yukonga.miuix.kmp.basic.TooltipDefaults
import top.yukonga.miuix.kmp.basic.rememberTooltipState

/**
 * Shows the [tooltip] content while [valueOrNull] is non-null and the anchor is
 * hovered / long-pressed (Miuix-backed, Phase 1c).
 *
 * The caller-supplied tooltip content draws itself (background, layout, etc.);
 * Miuix owns positioning, state and gesture handling. When [valueOrNull] is null
 * the tooltip interaction is disabled entirely.
 */
@Composable
fun <T : Any> Tooltip(
    modifier: Modifier = Modifier,
    valueOrNull: T?,
    tooltip: @Composable BoxScope.(T) -> Unit,
    content: @Composable () -> Unit,
) {
    val tooltipBody = tooltip
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Below,
        ),
        tooltip = {
            val value = valueOrNull
            if (value != null) {
                Box {
                    tooltipBody(value)
                }
            }
        },
        state = rememberTooltipState(),
        modifier = modifier,
        enableUserInput = valueOrNull != null,
        content = content,
    )
}