package com.artemchep.keyguard.feature.home.vault.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import arrow.core.Either
import arrow.optics.optics
import com.artemchep.keyguard.common.model.DSecret
import com.artemchep.keyguard.common.model.TotpToken
import com.artemchep.keyguard.common.service.app.parser.AppStoreListingInfo
import com.artemchep.keyguard.common.service.passkey.PassKeyServiceInfo
import com.artemchep.keyguard.common.usecase.CopyText
import com.artemchep.keyguard.feature.attachments.model.AttachmentItem
import com.artemchep.keyguard.feature.localization.TextHolder
import com.artemchep.keyguard.feature.navigation.keyboard.KeyShortcut
import com.artemchep.keyguard.ui.ContextItem
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.markdown.MarkdownDocument
import com.artemchep.keyguard.ui.markdown.MarkdownParser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@optics
sealed interface VaultViewItem {
    companion object

    val id: String

    data class Card(
        override val id: String,
        val elevation: Dp,
        val data: DSecret.Card,
        val visibility: Visibility = Visibility(),
        val dropdown: ImmutableList<ContextItem> = persistentListOf(),
    ) : VaultViewItem {
        companion object;
    }

    data class Identity(
        override val id: String,
        val data: DSecret.Identity,
        /**
         * List of the callable actions appended
         * to the item.
         */
        val actions: List<FlatItemAction> = emptyList(),
    ) : VaultViewItem {
        companion object
    }

    data class QuickActions(
        override val id: String,
        val actions: ImmutableList<ContextItem> = persistentListOf(),
    ) : VaultViewItem {
        companion object
    }

    data class QuickBadges(
        override val id: String,
        val actions: ImmutableList<Item> = persistentListOf(),
    ) : VaultViewItem {
        companion object;

        data class Item(
            val title: TextHolder,
            val text: TextHolder? = null,
        )
    }

    data class Action(
        override val id: String,
        val elevation: Dp = 0.dp,
        val title: String,
        val text: String? = null,
        val leading: (@Composable RowScope.() -> Unit)? = null,
        val trailing: (@Composable RowScope.() -> Unit)? = null,
        val badge: Badge? = null,
        /**
         * List of the callable actions appended
         * to the item.
         */
        val onClick: (() -> Unit)? = null,
    ) : VaultViewItem {
        companion object;

        data class Badge(
            val text: String,
            val score: Float,
        )
    }

    data class Link(
        override val id: String,
        val presentation: VaultItemPresentation?,
        val onClick: (() -> Unit)? = null,
    ) : VaultViewItem {
        companion object;
    }

    data class Value(
        override val id: String,
        val elevation: Dp = 0.dp,
        val title: String?,
        val value: String,
        val maxLines: Int = Int.MAX_VALUE,
        val visibility: Visibility = Visibility(),
        val monospace: Boolean = false,
        val colorize: Boolean = false,
        val leading: (@Composable RowScope.() -> Unit)? = null,
        val trailing: (@Composable RowScope.() -> Unit)? = null,
        val badge: Badge? = null,
        val badge2: List<StateFlow<Badge?>> = emptyList(),
        /**
         * A short outlined label shown next to
         * the value.
         */
        val label: String? = null,
        /**
         * List of the callable actions appended
         * to the item.
         */
        val dropdown: List<ContextItem> = emptyList(),
        /**
         * The item and the field the value is read from,
         * `null` for the own values of an item.
         */
        val reference: Reference? = null,
    ) : VaultViewItem {
        companion object;

        sealed interface Style {
            data object Info : Style
            data object Action : Style
        }

        data class Badge(
            val text: String,
            val score: Float,
        )

        data class Reference(
            /** The referenced item, `null` if it is not available. */
            val item: VaultItemPresentation?,
            val field: String,
            /** Opens the referenced item, `null` if it is not available. */
            val onClick: (() -> Unit)? = null,
        )
    }

    data class Table(
        override val id: String,
        val elevation: Dp = 0.dp,
        val title: String? = null,
        val value: String? = null,
        val rows: List<Row>,
        val dropdown: List<ContextItem> = emptyList(),
    ) : VaultViewItem {
        companion object;

        data class Row(
            val title: String,
            val value: String,
        )
    }

    data class Error(
        override val id: String,
        val name: String,
        val message: String?,
        val blob: String? = null,
        val timestamp: String,
        val onRetry: (() -> Unit)? = null,
        val onCopyBlob: (() -> Unit)? = null,
    ) : VaultViewItem {
        companion object;
    }

    data class Info(
        override val id: String,
        val name: String,
        val message: String? = null,
    ) : VaultViewItem {
        companion object;
    }

    data class Planeta(
        override val id: String,
        val fingerprint: String,
    ) : VaultViewItem {
        companion object;
    }

    data class Switch(
        override val id: String,
        val title: String,
        val value: Boolean,
        val dropdown: List<ContextItem> = emptyList(),
    ) : VaultViewItem {
        companion object;
    }

    data class Passkey(
        override val id: String,
        val value: String?,
        val source: DSecret.Login.Fido2Credentials,
        val onUse: (() -> Unit)? = null,
        val onClick: (() -> Unit)? = null,
    ) : VaultViewItem {
        companion object;
    }

    data class Note(
        override val id: String,
        val content: Content,
        val elevation: Dp = 0.dp,
        val visibility: Visibility = Visibility(),
    ) : VaultViewItem {
        companion object;

        sealed interface Content {
            companion object {
                fun of(
                    parser: MarkdownParser,
                    markdown: Boolean,
                    text: String,
                ): Content =
                    if (markdown) {
                        kotlin.runCatching {
                            val data = text.trimIndent()
                            val document = parser.parse(data)
                            Markdown(document)
                        }.getOrNull()
                    } else {
                        null
                    } ?: Text(text)
            }

            data class Markdown(
                val document: MarkdownDocument,
            ) : Content

            data class Text(
                val text: String,
            ) : Content
        }
    }

    data class Spacer(
        override val id: String,
        val height: Dp,
    ) : VaultViewItem {
        companion object
    }

    data class Label(
        override val id: String,
        val horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
        val text: AnnotatedString,
        val error: Boolean = false,
    ) : VaultViewItem {
        companion object
    }

    data class Tags(
        override val id: String,
        val tags: List<String>,
        val onClick: (String) -> Unit,
    ) : VaultViewItem {
        companion object;
    }

    data class Folder(
        override val id: String,
        val nodes: List<FolderNode>,
        val onClick: () -> Unit,
    ) : VaultViewItem {
        companion object;

        data class FolderNode(
            val name: String,
            val onClick: () -> Unit,
        )
    }

    data class Organization(
        override val id: String,
        val title: String,
        val onClick: () -> Unit,
    ) : VaultViewItem {
        companion object;
    }

    data class Collection(
        override val id: String,
        val title: String,
        val onClick: () -> Unit,
    ) : VaultViewItem {
        companion object;
    }

    data class Uri(
        override val id: String,
        val title: AnnotatedString,
        val text: String? = null,
        val matchTypeTitle: String? = null,
        val warningTitle: String? = null,
        val icon: @Composable () -> Unit,
        /**
         * List of the callable actions appended
         * to the item.
         */
        val dropdown: List<ContextItem> = emptyList(),
        val overrides: List<Override> = emptyList(),
        val colorize: Boolean = false,
        val iconSource: VaultUriIcon? = null,
    ) : VaultViewItem {
        companion object;

        data class Override(
            val title: String,
            val text: String,
            val error: Boolean,
            /**
             * List of the callable actions appended
             * to the item.
             */
            val dropdown: List<ContextItem> = emptyList(),
        )

        data class AppStoreListing(
            val store: String,
            val state: Flow<Either<Throwable, AppStoreListingInfo?>>,
            val onClick: () -> Unit,
        )
    }

    data class Totp(
        override val id: String,
        val copy: CopyText,
        val title: String,
        val shortcut: KeyShortcut? = null,
        val elevation: Dp,
        val totp: TotpToken,
        val localStateFlow: StateFlow<LocalState>,
    ) : VaultViewItem {
        companion object;

        @Immutable
        data class LocalState(
            val codes: PersistentList<PersistentList<String>>,
            val dropdown: PersistentList<ContextItem>,
        )
    }

    data class InactiveTotp(
        override val id: String,
        val chevron: Boolean,
        val onClick: (() -> Unit)?,
    ) : VaultViewItem {
        companion object;
    }

    data class InactivePasskey(
        override val id: String,
        val info: PassKeyServiceInfo,
        val onClick: () -> Unit,
    ) : VaultViewItem {
        companion object;
    }

    data class ReusedPassword(
        override val id: String,
        val count: Int,
        val onClick: () -> Unit,
    ) : VaultViewItem {
        companion object;
    }

    data class Qr(
        override val id: String,
        val data: String,
    ) : VaultViewItem {
        companion object;
    }

    data class Section(
        override val id: String,
        val text: String? = null,
    ) : VaultViewItem {
        companion object
    }

    data class Button(
        override val id: String,
        val leading: (@Composable RowScope.() -> Unit)? = null,
        val text: String,
        val onClick: () -> Unit,
    ) : VaultViewItem {
        companion object
    }

    data class Attachment(
        override val id: String,
        val item: AttachmentItem,
    ) : VaultViewItem {
        companion object;
    }

}
