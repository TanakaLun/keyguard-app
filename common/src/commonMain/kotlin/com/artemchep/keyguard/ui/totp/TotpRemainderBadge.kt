package com.artemchep.keyguard.ui.totp

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.ProgressIndicatorDefaults
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.ui.AhContainer
import com.artemchep.keyguard.ui.theme.monoFontFamily

/**
 * Shows the time left until the code changes, or
 * the counter of a counter-based code.
 */
@Composable
fun TotpRemainderBadge(
    state: TotpCodeState,
    textStyle: TextStyle = MiuixTheme.textStyles.labelMedium,
) {
    val score = when (state) {
        is TotpCodeState.Loading -> null
        is TotpCodeState.Error -> null

        is TotpCodeState.Success -> when (val counter = state.counter) {
            is TotpCodeState.Success.TimeBasedCounter -> counter.progress.coerceIn(
                0f,
                1f,
            )

            is TotpCodeState.Success.IncrementBasedCounter -> null
        }
    }
    AhContainer(
        score = score
            ?: 1f,
    ) { progress ->
        val time = (state as? TotpCodeState.Success)?.counter?.text.orEmpty()
        TotpRemainderBadgeContent(
            progress = progress.takeIf { score != null },
            text = time,
            textStyle = textStyle,
        )
    }
}

@Composable
private fun RowScope.TotpRemainderBadgeContent(
    progress: (() -> Float)?,
    text: String,
    textStyle: TextStyle,
) {
    val circularProgressModifier = Modifier.size(16.dp)
    when (progress) {
        null -> {
            // Do nothing.
        }

        else -> {
            CircularProgressIndicator(
                modifier = circularProgressModifier,
                progress = progress(),
                colors = ProgressIndicatorDefaults.progressIndicatorColors(
                    foregroundColor = MiuixTheme.colorScheme.onSurface,
                    backgroundColor = Color.Transparent,
                ),
            )
            Spacer(Modifier.width(4.dp))
        }
    }
    Spacer(Modifier.width(4.dp))
    Text(
        modifier = Modifier
            .animateContentSize(),
        text = text,
        fontFamily = monoFontFamily,
        fontWeight = FontWeight.Bold,
        style = textStyle,
    )
    Spacer(Modifier.width(4.dp))
}
