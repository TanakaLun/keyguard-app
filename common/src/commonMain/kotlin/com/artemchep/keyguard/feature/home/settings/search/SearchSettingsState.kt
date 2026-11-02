package com.artemchep.keyguard.feature.home.settings.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.artemchep.keyguard.feature.auth.common.TextFieldModel

data class SearchSettingsState(
    val revision: Int = 0,
    val query: TextFieldModel = TextFieldModel(text = ""),
    val items: List<Item> = emptyList(),
) {
    sealed interface Item {
        val key: String
        val contentType: String

        data class Settings(
            override val key: String,
            val score: Float,
            val content: @Composable () -> Unit,
        ) : Item {
            override val contentType: String get() = "settings_search_item"
        }
    }
}
