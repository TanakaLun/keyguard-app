package com.artemchep.keyguard.feature.imports

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.AppShortcut
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.home.settings.backups.icon
import com.artemchep.keyguard.feature.home.settings.backups.titleRes
import com.artemchep.keyguard.platform.LeContext
import com.artemchep.keyguard.platform.LocalLeContext
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * The idle step of the import flow: where the items can come from.
 */
internal fun LazyListScope.importStartContent(
    stage: ImportState.Stage.Start,
) {
    item("start.header") {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = Dimens.textHorizontalPadding,
                    vertical = 8.dp,
                ),
            text = stringResource(Res.string.import_header_text),
            style = MiuixTheme.textStyles.body1,
        )
    }
    stage.onCredentialExchange?.let { onCredentialExchange ->
        item("start.device.section") {
            SmallTitle(
                text = stringResource(Res.string.import_source_device_section),
            )
        }
        item("start.device.credential_exchange") {
            ImportCredentialExchangeItem(
                onClick = onCredentialExchange,
            )
        }
    }
    if (stage.formats.isNotEmpty()) {
        item("start.file.section") {
            SmallTitle(
                text = stringResource(Res.string.import_source_file_section),
            )
        }
        items(
            items = stage.formats,
            key = { format -> "start.file.${format.key}" },
        ) { format ->
            ImportFormatItem(
                format = format,
            )
        }
    }
    importBackupLocationsContent(
        locations = stage.backupLocations,
    )
    stage.onLearnMore?.let { onLearnMore ->
        item("start.learn_more") {
            Column {
                ImportLearnMoreButton(
                    onClick = onLearnMore,
                )
            }
        }
    }
}

private fun LazyListScope.importBackupLocationsContent(
    locations: List<ImportState.Stage.Start.BackupLocation>,
) {
    if (locations.isEmpty()) {
        return
    }
    item("start.backup.section") {
        SmallTitle(
            text = stringResource(Res.string.import_source_backup_section),
        )
    }
    items(
        items = locations,
        key = { location -> "start.backup.${location.kind.name}" },
    ) { location ->
        ImportBackupLocationItem(
            location = location,
        )
    }
}

@Composable
private fun ImportCredentialExchangeItem(
    onClick: (LeContext) -> Unit,
) {
    val context by rememberUpdatedState(LocalLeContext)
    val updatedOnClick by rememberUpdatedState(onClick)
    BasicComponent(
        title = stringResource(Res.string.import_source_credential_exchange_title),
        summary = stringResource(Res.string.credential_exchange_import_header_text),
        startAction = {
            Icon(Icons.Outlined.AppShortcut, contentDescription = null)
        },
        endActions = {
            ChevronIcon()
        },
        onClick = {
            updatedOnClick(context)
        },
    )
}

@Composable
private fun ImportFormatItem(
    format: ImportState.Stage.Start.Format,
) {
    BasicComponent(
        title = format.title,
        summary = format.text,
        endActions = {
            ChevronIcon()
        },
        onClick = format.onClick,
    )
}

@Composable
private fun ImportBackupLocationItem(
    location: ImportState.Stage.Start.BackupLocation,
) {
    BasicComponent(
        title = stringResource(location.kind.titleRes),
        startAction = {
            Icon(location.kind.icon, contentDescription = null)
        },
        endActions = {
            ChevronIcon()
        },
        onClick = location.onClick,
    )
}

@Composable
private fun ColumnScope.ImportLearnMoreButton(
    onClick: () -> Unit,
) {
    val updatedOnClick by rememberUpdatedState(onClick)
    Spacer(
        modifier = Modifier
            .height(16.dp),
    )
    Button(
        modifier = Modifier
            .padding(
                horizontal = Dimens.buttonHorizontalPadding,
            ),
        colors = ButtonDefaults.buttonColors(),
        onClick = {
            updatedOnClick()
        },
    ) {
        Icon(
            modifier = Modifier
                .size(18.dp),
            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
            contentDescription = null,
        )
        Spacer(
            modifier = Modifier
                .width(Dimens.buttonIconPadding),
        )
        Text(
            text = stringResource(Res.string.import_learn_more),
        )
    }
}
