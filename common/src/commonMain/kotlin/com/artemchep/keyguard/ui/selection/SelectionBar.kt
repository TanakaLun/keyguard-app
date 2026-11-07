package com.artemchep.keyguard.ui.selection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import top.yukonga.miuix.kmp.basic.FloatingToolbar
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.platform.leNavigationBars

/**
 * The long-press selection bar, as a standard Miuix [FloatingToolbar]: a close
 * action, the selection title, and the per-selection actions.
 */
@Composable
fun SelectionBar(
    title: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClear: (() -> Unit)?,
) {
    val navBarInsets = WindowInsets.leNavigationBars
        .only(WindowInsetsSides.Bottom)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(navBarInsets)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        FloatingToolbar {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = 6.dp,
                        vertical = 2.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    enabled = onClear != null,
                    onClick = {
                        onClear?.invoke()
                    },
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        null,
                    )
                }
                if (title != null) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        title()
                    }
                }
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                if (trailing != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        trailing()
                    }
                }
            }
        }
    }
}
