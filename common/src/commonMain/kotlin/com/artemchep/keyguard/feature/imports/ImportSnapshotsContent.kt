package com.artemchep.keyguard.feature.imports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.ui.theme.Dimens
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * The snapshots of an open Keyguard backup, one of them selected.
 */
internal fun LazyListScope.importSnapshotsContent(
    stage: ImportState.Stage.Snapshots,
) {
    item("snapshots.header") {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = Dimens.textHorizontalPadding,
                    vertical = 8.dp,
                ),
            text = stringResource(Res.string.import_backup_snapshots_text),
            style = MiuixTheme.textStyles.body1,
        )
    }
    itemsIndexed(
        items = stage.items,
        key = { _, snapshot -> "snapshots.${snapshot.key}" },
    ) { _, snapshot ->
        ImportSnapshotItem(
            snapshot = snapshot,
        )
    }
    item("snapshots.cancel") {
        Column {
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
            ImportTextButton(
                text = stringResource(Res.string.cancel),
                onClick = stage.onCancel,
            )
        }
    }
}

@Composable
private fun ImportSnapshotItem(
    snapshot: ImportState.Stage.Snapshots.Snapshot,
) {
    val backgroundColor = if (snapshot.selected) {
        MiuixTheme.colorScheme.secondaryContainer
    } else {
        Color.Unspecified
    }
    val itemsText = pluralStringResource(
        Res.plurals.import_backup_snapshot_items,
        snapshot.itemCount,
        snapshot.itemCount,
    )
    val text = if (snapshot.attachmentCount > 0) {
        val attachmentsText = pluralStringResource(
            Res.plurals.import_backup_snapshot_attachments,
            snapshot.attachmentCount,
            snapshot.attachmentCount,
        )
        "$itemsText · $attachmentsText"
    } else {
        itemsText
    }
    BasicComponent(
        modifier = Modifier
            .semantics {
                selected = snapshot.selected
                role = Role.RadioButton
            }
            .then(
                if (backgroundColor.isSpecified) {
                    Modifier.background(backgroundColor)
                } else {
                    Modifier
                },
            ),
        title = snapshot.createdAt,
        summary = text,
        endActions = {
            RadioButton(
                selected = snapshot.selected,
                onClick = null,
            )
        },
        onClick = snapshot.onClick,
    )
}
