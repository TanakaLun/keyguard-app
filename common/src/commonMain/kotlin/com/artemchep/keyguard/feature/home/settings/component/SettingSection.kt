package com.artemchep.keyguard.feature.home.settings.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.settings.LocalSettingItemArgs
import com.artemchep.keyguard.feature.localization.TextHolder
import com.artemchep.keyguard.feature.localization.textResource
import kotlinx.coroutines.flow.flowOf
import org.koin.core.scope.Scope
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.SmallTitle

fun settingSectionProvider(
    koinScope: Scope,
) = settingSectionProvider()

fun settingSectionProvider(): SettingComponent = kotlin.run {
    val item = SettingIi {
        val text = run {
            val args = LocalSettingItemArgs.current as? SettingSectionArgs
            args?.title
                ?.let { textResource(it) }
        }

        if (text != null) {
            SmallTitle(
                text = text,
            )
        } else {
            HorizontalDivider(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                    ),
            )
        }
    }
    flowOf(item)
}

data class SettingSectionArgs(
    val title: TextHolder? = null,
)
