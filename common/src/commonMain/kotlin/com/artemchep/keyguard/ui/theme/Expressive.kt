package com.artemchep.keyguard.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

// The Material 3 Expressive theme toggle was removed during the Miuix
// migration; these locals stay as constant true so legacy components keep
// their expressive visuals until they are migrated too.
val LocalExpressive = staticCompositionLocalOf {
    true
}

val GlobalExpressive = staticCompositionLocalOf {
    true
}
