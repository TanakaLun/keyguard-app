package com.artemchep.keyguard.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.basic.Switch as MiuixSwitch

/**
 * A Miuix-backed switch (Phase 1).
 *
 * The Material 3 Expressive thumb icons are not part of Miuix; the switch now
 * follows the HyperOS look instead.
 */
@Composable
fun SwitchExpressive(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MiuixSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    )
}