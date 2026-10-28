package com.artemchep.keyguard.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollBehavior.rememberFabExpanded() = remember(this) {
    derivedStateOf {
        state.overlappedFraction < 0.01f
    }
}
