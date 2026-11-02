package com.artemchep.keyguard.feature.androidipc

import androidx.compose.runtime.Immutable

@Immutable
data class ConnectedCryptoAppsState(
    val apps: List<App>,
) {
    @Immutable
    data class App(
        val key: String,
        val packageName: String,
        val label: String,
        val signer: String,
        val registeredAt: String,
        val lastUsedAt: String,
        val installed: Boolean,
        val signerMismatch: Boolean,
        val onRevoke: () -> Unit,
    )
}
