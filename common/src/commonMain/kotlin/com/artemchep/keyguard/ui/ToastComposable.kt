package com.artemchep.keyguard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import com.artemchep.keyguard.common.usecase.MessageHub
import com.artemchep.keyguard.feature.navigation.navigationNodeStack
import com.artemchep.keyguard.platform.LocalWindowId
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarHost
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.SnackbarResult

/**
 * All transient status messages (copied, saved, failed, …) are shown as
 * standard Miuix snackbars instead of a bespoke bottom widget.
 */
@Composable
fun ToastMessageHost(
    modifier: Modifier = Modifier,
) {
    val hub = koinInject<MessageHub>()
    val nav = navigationNodeStack()
    val windowId = LocalWindowId.current
    val windowIsFocused = LocalWindowInfo.current.isWindowFocused
    val scope = rememberCoroutineScope()
    val hostState = remember {
        SnackbarHostState()
    }

    DisposableEffect(hub, nav, windowId, windowIsFocused) {
        if (!windowIsFocused) {
            return@DisposableEffect onDispose {
                // Do nothing.
            }
        }

        val unregister = hub.register(key = nav, windowId = windowId) { message ->
            scope.launch {
                val text = listOfNotNull(message.title, message.text)
                    .joinToString(separator = "\n")
                val result = hostState.showSnackbar(
                    message = text,
                    actionLabel = message.action?.title,
                    withDismissAction = message.action != null,
                    duration = SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    message.action?.onClick?.invoke()
                }
            }
        }
        onDispose {
            unregister()
        }
    }

    SnackbarHost(
        modifier = modifier,
        state = hostState,
    )
}
