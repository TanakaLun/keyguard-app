package com.artemchep.keyguard.feature.timepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.artemchep.keyguard.common.model.getOrNull
import com.artemchep.keyguard.feature.dialog.Dialog
import com.artemchep.keyguard.feature.navigation.RouteResultTransmitter
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalTime

@Composable
fun TimePickerScreen(
    args: TimePickerRoute.Args,
    transmitter: RouteResultTransmitter<TimePickerResult>,
) {
    val loadableState = produceTimePickerState(
        args = args,
        transmitter = transmitter,
    )
    Dialog(
        title = null,
        content = {
            Column(
                modifier = Modifier,
            ) {
                val state = loadableState.getOrNull()
                    ?: return@Column
                Content(
                    state = state,
                )
            }
        },
        contentScrollable = true,
        actions = {
            val updatedOnClose by rememberUpdatedState(loadableState.getOrNull()?.onDeny)
            val updatedOnOk by rememberUpdatedState(loadableState.getOrNull()?.onConfirm)
            TextButton(
    text = stringResource(Res.string.close),

                enabled = updatedOnClose != null,
                onClick = {
                    updatedOnClose?.invoke()
                },
            )
            TextButton(
    text = stringResource(Res.string.ok),

                enabled = updatedOnOk != null,
                onClick = {
                    updatedOnOk?.invoke()
                },
            )
        },
    )
}

@Composable
private fun ColumnScope.Content(
    state: TimePickerState,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = state.content.initialTime.hour,
        initialMinute = state.content.initialTime.minute,
    )

    val updatedOnSelect by rememberUpdatedState(state.content.onSelect)
    LaunchedEffect(timePickerState) {
        snapshotFlow {
            val hour = timePickerState.hour
            val minute = timePickerState.minute
            LocalTime(hour = hour, minute = minute)
        }
            .onEach { time ->
                updatedOnSelect(time)
            }
            .collect()
    }

    TimePicker(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.horizontalPadding),
        state = timePickerState,
        layoutType = TimePickerLayoutType.Vertical,
    )
}
