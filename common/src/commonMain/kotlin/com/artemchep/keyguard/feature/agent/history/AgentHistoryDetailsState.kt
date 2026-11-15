package com.artemchep.keyguard.feature.agent.history

import androidx.compose.runtime.Immutable
import com.artemchep.keyguard.ui.ContextItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AgentHistoryDetailsState(
    val details: AgentHistoryDetails,
    val items: ImmutableList<AgentHistoryDetailsItem>,
    val onClose: () -> Unit,
)

@Immutable
sealed interface AgentHistoryDetailsItem {
    val id: String

    /** A header, or a gap between two groups of rows if there is no [title]. */
    @Immutable
    data class Section(
        override val id: String,
        val title: String?,
    ) : AgentHistoryDetailsItem

    /** The labels and values of a [AgentHistoryDetails.Section.table] section. */
    @Immutable
    data class Table(
        override val id: String,
        val rows: ImmutableList<AgentHistoryDetails.Row.Text>,
        val dropdown: ImmutableList<ContextItem>,
    ) : AgentHistoryDetailsItem

    @Immutable
    data class Value(
        override val id: String,
        val row: AgentHistoryDetails.Row,
        val dropdown: ImmutableList<ContextItem>,
        /** Opens the referenced item, `null` if there is nothing to open. */
        val onClick: (() -> Unit)? = null,
    ) : AgentHistoryDetailsItem
}
