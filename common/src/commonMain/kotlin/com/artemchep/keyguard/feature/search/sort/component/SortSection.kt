package com.artemchep.keyguard.feature.search.sort.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.SmallTitle

@Composable
fun SortSectionComposable(
    modifier: Modifier = Modifier,
    text: String?,
) {
    if (text != null) {
        SmallTitle(
            modifier = modifier,
            text = text,
        )
    } else {
        HorizontalDivider(
            modifier = modifier
                .padding(
                    vertical = 8.dp,
                ),
        )
    }
}
