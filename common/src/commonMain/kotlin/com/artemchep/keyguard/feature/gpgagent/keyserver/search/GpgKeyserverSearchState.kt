package com.artemchep.keyguard.feature.gpgagent.keyserver.search

import androidx.compose.runtime.Immutable
import arrow.core.Either
import com.artemchep.keyguard.common.model.DGpgKeyserverResult
import com.artemchep.keyguard.common.model.Loadable
import com.artemchep.keyguard.feature.auth.common.TextFieldModel
import com.artemchep.keyguard.ui.ContextItem
import kotlinx.coroutines.flow.StateFlow

data class GpgKeyserverSearchState(
    val keyserverUrl: String,
    val filter: StateFlow<Filter>,
    val content: Loadable<Either<Throwable, Content>>,
    val onScanned: (String) -> Unit,
) {
    @Immutable
    data class Filter(
        val revision: Int,
        val query: TextFieldModel,
    )

    @Immutable
    data class Content(
        val revision: Int,
        val items: List<Item>,
    )

    @Immutable
    sealed interface Item {
        val key: String
        val contentType: String

        @Immutable
        data class Content(
            override val key: String,
            val title: String,
            val description: String,
            val dropdown: List<ContextItem>,
            val result: DGpgKeyserverResult,
        ) : Item {
            override val contentType: String get() = "gpg_keyserver_search_content"
        }
    }
}
