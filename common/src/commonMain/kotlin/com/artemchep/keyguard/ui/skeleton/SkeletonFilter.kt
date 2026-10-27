package com.artemchep.keyguard.ui.skeleton

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.feature.search.filter.component.FilterItemLayout
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.labelLarge

@Composable
fun SkeletonFilter(
    modifier: Modifier = Modifier,
) {
    FilterItemLayout(
        modifier = modifier,
        checked = false,
        leading = null,
        content = {
            SkeletonText(
                modifier = Modifier
                    .width(64.dp),
                style = MiuixTheme.textStyles.labelLarge,
            )
        },
        onClick = null,
        enabled = true,
    )
}
