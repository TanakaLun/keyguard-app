package com.artemchep.keyguard.ui.toolbar

import androidx.compose.foundation.layout.RowScope
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.platform.CurrentPlatform
import com.artemchep.keyguard.platform.Platform
import com.artemchep.keyguard.ui.toolbar.util.ToolbarColors
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun LargeToolbar(
    title: String,
    subtitle: String = "",
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: ScrollBehavior? = null,
) {
    if (CurrentPlatform is Platform.Desktop) {
        SmallToolbar(
            title = title,
            subtitle = subtitle,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
        )
        return
    }

    val containerColor = ToolbarColors.containerColor()
    TopAppBar(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        color = containerColor,
        scrollBehavior = scrollBehavior,
    )
}