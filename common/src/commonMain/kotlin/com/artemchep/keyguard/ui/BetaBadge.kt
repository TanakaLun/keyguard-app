package com.artemchep.keyguard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.ready_status_beta
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.labelSmall

@Composable
fun BetaBadge(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .background(MiuixTheme.colorScheme.primary, shape = CircleShape)
            .padding(
                vertical = 1.dp,
                horizontal = 6.dp,
            ),
        text = stringResource(Res.string.ready_status_beta),
        fontWeight = FontWeight.Bold,
        style = MiuixTheme.textStyles.labelSmall,
        color = MiuixTheme.colorScheme.onPrimary,
    )
}
