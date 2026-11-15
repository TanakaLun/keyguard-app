package com.artemchep.keyguard.ui.wizard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.navigation.NavigationIcon
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.KeyguardLoadingIndicator
import com.artemchep.keyguard.ui.ScaffoldLazyColumn
import com.artemchep.keyguard.ui.scaffoldContentWindowInsets
import com.artemchep.keyguard.ui.screenMaxWidthCompact
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.headlineMedium
import com.artemchep.keyguard.ui.theme.labelLarge
import com.artemchep.keyguard.ui.toolbar.LargeToolbar
import com.artemchep.keyguard.ui.toolbar.util.ToolbarBehavior
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * A screen that shows one step of a multi-step flow at a time.
 * The [content] of a step starts with a [WizardHeader]; a `null`
 * [step] shows a loading indicator.
 */
@Composable
fun <T : Any> WizardScaffold(
    title: String,
    step: T?,
    navigationIcon: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    val scrollBehavior = ToolbarBehavior.behavior()
    val listState = remember(step) { LazyListState() }
    ScaffoldLazyColumn(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBarScrollBehavior = scrollBehavior,
        listState = listState,
        topBar = {
            LargeToolbar(
                title = title,
                navigationIcon = navigationIcon,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = bottomBar,
    ) {
        item("wizard") {
            WizardColumn {
                if (step == null) {
                    KeyguardLoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Crossfade(
                        targetState = step,
                        label = "wizard_step",
                    ) { currentStep ->
                        Column(verticalArrangement = Arrangement.spacedBy(Dimens.verticalPadding)) {
                            content(currentStep)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Goes to the previous step, or leaves the
 * screen when [onBack] is `null`.
 */
@Composable
fun WizardBackButton(
    onBack: (() -> Unit)?,
    enabled: Boolean = true,
) {
    val focusManager = LocalFocusManager.current
    if (onBack == null) {
        NavigationIcon()
    } else {
        IconButton(
            enabled = enabled,
            onClick = {
                focusManager.clearFocus()
                onBack()
            },
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(Res.string.additem_key_generator_back_title),
            )
        }
    }
}

@Composable
fun WizardColumn(content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier.widthIn(max = screenMaxWidthCompact).fillMaxWidth()
                .padding(vertical = Dimens.verticalPadding),
            content = content,
        )
    }
}

/**
 * The primary action of a step, disabled
 * while [onClick] is `null`, with optional
 * [guidance] above it.
 */
@Composable
fun WizardBottomBar(
    text: String,
    onClick: (() -> Unit)?,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
    tonal: Boolean = false,
    guidance: @Composable ColumnScope.() -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    Surface {
        Box(
            modifier = modifier.fillMaxWidth().windowInsetsPadding(
                scaffoldContentWindowInsets.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
            ),
        ) {
            WizardColumn {
                guidance()
                Button(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.fieldHorizontalPadding),
                    colors = if (tonal) {
                        ButtonDefaults.buttonColors()
                    } else {
                        ButtonDefaults.buttonColorsPrimary()
                    },
                    enabled = onClick != null,
                    onClick = {
                        focusManager.clearFocus()
                        onClick?.invoke()
                    },
                ) {
                    if (loading) {
                        KeyguardLoadingIndicator(modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(text)
                }
            }
        }
    }
}

/**
 * Asks to confirm leaving a wizard
 * with unsaved choices.
 */
@Composable
fun WizardDiscardDialog(
    title: String,
    text: String,
    action: String,
    onDiscard: () -> Unit,
    onDismiss: () -> Unit,
) {
    OverlayDialog(
        show = true,
        onDismissRequest = onDismiss,
        title = title,
        summary = text,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                text = stringResource(Res.string.cancel),
                onClick = onDismiss,
            )
            TextButton(
                text = action,
                onClick = onDiscard,
            )
        }
    }
}

/**
 * The step label, progress, title and detail of a step.
 */
@Composable
fun WizardHeader(
    stepNumber: Int,
    stepCount: Int,
    title: String,
    detail: String?,
) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.textHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.verticalPadding),
    ) {
        Text(
            // The label is shared by every wizard, the key
            // keeps its name to keep the translations.
            text = stringResource(
                Res.string.pref_item_automatic_backups_wizard_step_label,
                stepNumber,
                stepCount,
            ),
            style = MiuixTheme.textStyles.labelLarge,
            color = MiuixTheme.colorScheme.primary,
        )
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = stepNumber.toFloat() / stepCount,
        )
        Text(
            modifier = Modifier.semantics { heading() },
            text = title,
            style = MiuixTheme.textStyles.headlineMedium,
        )
        if (detail != null) {
            Text(
                text = detail,
                style = MiuixTheme.textStyles.bodyLarge,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            )
        }
    }
}
