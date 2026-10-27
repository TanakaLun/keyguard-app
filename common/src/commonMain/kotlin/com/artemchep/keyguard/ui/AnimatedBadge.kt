package com.artemchep.keyguard.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import top.yukonga.miuix.kmp.basic.Badge
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
inline fun AnimatedNewCounterBadge(
    count: Int?,
    predicate: (Int) -> Boolean = { it > 0 },
) = AnimatedCounterBadge(
    text = count?.takeIf(predicate)?.let { "+$it" },
)

@Composable
inline fun AnimatedTotalCounterBadge(
    count: Int?,
    predicate: (Int) -> Boolean = { it >= 0 },
) = AnimatedCounterBadge(
    text = count?.takeIf(predicate)?.toString(),
)

@Composable
fun AnimatedCounterBadge(
    text: String?,
) {
    ExpandedIfNotEmpty(
        valueOrNull = text,
        enter = fadeIn(
            animationSpec = tween(),
        ) + scaleIn(
            animationSpec = tween(),
        ),
        exit = fadeOut(
            animationSpec = tween(),
        ) + scaleOut(
            animationSpec = tween(),
        ),
    ) {
        Badge(
            containerColor = MiuixTheme.colorScheme.tertiaryContainer,
        ) {
            Text(
                text = it,
            )
        }
    }
}