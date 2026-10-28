package com.artemchep.keyguard.ui.toolbar

import androidx.compose.foundation.layout.RowScope
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.artemchep.keyguard.ui.toolbar.util.ToolbarColors
import top.yukonga.miuix.kmp.basic.SmallTopAppBar

@Composable
fun SmallToolbar(
    title: String,
    subtitle: String = "",
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    containerColor: Color = ToolbarColors.containerColor(),
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: ScrollBehavior? = null,
) {
    SmallTopAppBar(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        color = containerColor,
        scrollBehavior = scrollBehavior,
    )
}