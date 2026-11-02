package com.artemchep.keyguard.feature.androidipc

import com.artemchep.keyguard.common.util.UniqueKeyBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class ConnectedCryptoAppsStateProducerTest {
    @Test
    fun `unique package names are used as keys`() {
        assertEquals(
            listOf(
                "com.example.alpha",
                "com.example.beta",
            ),
            connectedCryptoAppKeysForTest(
                packageNames = listOf(
                    "com.example.alpha",
                    "com.example.beta",
                ),
            ),
        )
    }

    @Test
    fun `duplicate package names receive unique deterministic suffixes`() {
        assertEquals(
            listOf(
                "com.example.client",
                "com.example.client#1",
                "com.example.other",
                "com.example.client#2",
            ),
            connectedCryptoAppKeysForTest(
                packageNames = listOf(
                    "com.example.client",
                    "com.example.client",
                    "com.example.other",
                    "com.example.client",
                ),
            ),
        )
    }
}

private fun connectedCryptoAppKeysForTest(
    packageNames: List<String>,
): List<String> {
    val keyBuilder = UniqueKeyBuilder()
    return packageNames.map(keyBuilder::build)
}
