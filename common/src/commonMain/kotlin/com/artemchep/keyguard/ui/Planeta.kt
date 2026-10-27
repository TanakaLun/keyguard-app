package com.artemchep.keyguard.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.ui.theme.isDark
import com.artemchep.keyguard.util.planeta.Planeta
import com.artemchep.keyguard.util.planeta.PlanetaOptions
import com.artemchep.keyguard.util.planeta.PlanetaTheme
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun FingerprintPlaneta(
    modifier: Modifier = Modifier,
    fingerprint: String,
) {
    val theme = if (MiuixTheme.colorScheme.isDark) {
        PlanetaTheme.Dark
    } else PlanetaTheme.Light
    Planeta(
        fingerprint = fingerprint,
        modifier = modifier,
        options = PlanetaOptions(
            theme = theme,
        ),
    )
}
