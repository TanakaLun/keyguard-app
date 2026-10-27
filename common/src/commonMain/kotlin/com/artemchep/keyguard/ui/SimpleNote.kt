package com.artemchep.keyguard.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import top.yukonga.miuix.kmp.basic.Icon
import androidx.compose.material3.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.feature.home.vault.component.FlatItemLayoutExpressive
import com.artemchep.keyguard.ui.theme.combineAlpha
import com.artemchep.keyguard.ui.theme.info
import com.artemchep.keyguard.ui.theme.infoContainer
import com.artemchep.keyguard.ui.theme.ok
import com.artemchep.keyguard.ui.theme.okContainer
import com.artemchep.keyguard.ui.theme.onInfoContainer
import com.artemchep.keyguard.ui.theme.onOkContainer
import com.artemchep.keyguard.ui.theme.onWarningContainer
import com.artemchep.keyguard.ui.theme.warning
import com.artemchep.keyguard.ui.theme.warningContainer
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.bodySmall
import com.artemchep.keyguard.ui.theme.titleSmall

@Immutable
data class SimpleNote(
    val text: String,
    val type: Type,
) {
    @Immutable
    enum class Type {
        OK,
        INFO,
        WARNING,
        ERROR,
    }
}

@Composable
fun FlatSimpleNote(
    modifier: Modifier = Modifier,
    note: SimpleNote,
) {
    FlatSimpleNote(
        modifier = modifier,
        type = note.type,
        text = note.text,
    )
}

@Composable
fun FlatSimpleNote(
    modifier: Modifier = Modifier,
    type: SimpleNote.Type,
    title: String? = null,
    text: String? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    icon: Boolean = true,
) {
    val tintColor = when (type) {
        SimpleNote.Type.OK -> MiuixTheme.colorScheme.ok
        SimpleNote.Type.INFO -> MiuixTheme.colorScheme.info
        SimpleNote.Type.WARNING -> MiuixTheme.colorScheme.warning
        SimpleNote.Type.ERROR -> MiuixTheme.colorScheme.error
    }
    val surfaceColor = when (type) {
        SimpleNote.Type.OK -> MiuixTheme.colorScheme.okContainer
        SimpleNote.Type.INFO -> MiuixTheme.colorScheme.infoContainer
        SimpleNote.Type.WARNING -> MiuixTheme.colorScheme.warningContainer
        SimpleNote.Type.ERROR -> MiuixTheme.colorScheme.errorContainer
    }
    val contentColor = when (type) {
        SimpleNote.Type.OK -> MiuixTheme.colorScheme.onOkContainer
        SimpleNote.Type.INFO -> MiuixTheme.colorScheme.onInfoContainer
        SimpleNote.Type.WARNING -> MiuixTheme.colorScheme.onWarningContainer
        SimpleNote.Type.ERROR -> MiuixTheme.colorScheme.onErrorContainer
    }
    FlatItemLayoutExpressive(
        modifier = modifier,
        backgroundColor = surfaceColor
            .combineAlpha(DisabledEmphasisAlpha),
        contentColor = contentColor,
        leading = if (icon) {
            // composable
            leading@{
                if (leading != null) {
                    leading.invoke(this)
                    return@leading
                }

                val imageVector = when (type) {
                    SimpleNote.Type.OK -> Icons.Outlined.Check
                    SimpleNote.Type.INFO -> Icons.Outlined.Info
                    SimpleNote.Type.WARNING -> Icons.Outlined.Warning
                    SimpleNote.Type.ERROR -> Icons.Outlined.ErrorOutline
                }
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = tintColor,
                )
            }
        } else {
            null
        },
        content = {
            if (title != null) {
                Text(
                    text = title,
                    style = MiuixTheme.textStyles.titleSmall,
                    color = contentColor,
                )
            }
            if (text != null) {
                val textColor = if (title != null) {
                    LocalContentColor.current
                        .combineAlpha(MediumEmphasisAlpha)
                } else {
                    LocalContentColor.current
                }
                Text(
                    text = text,
                    style = MiuixTheme.textStyles.bodySmall,
                    color = textColor,
                )
            }
            if (content != null) {
                content()
            }
        },
        trailing = trailing,
        onClick = onClick,
        enabled = enabled,
    )
}
