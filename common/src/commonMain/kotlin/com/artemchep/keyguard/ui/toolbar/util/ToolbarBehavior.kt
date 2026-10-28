package com.artemchep.keyguard.ui.toolbar.util

import androidx.compose.runtime.Composable
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.ScrollBehavior

object ToolbarBehavior {
    /**
     * The standard Miuix top app bar scroll behavior: the bar collapses when the
     * content is pulled up and expands when it is pulled down.
     */
    @Composable
    fun behavior(): ScrollBehavior = MiuixScrollBehavior()
}
