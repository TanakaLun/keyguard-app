package com.artemchep.keyguard.feature.search.filter.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.LocalTextStyle
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.theme.combineAlpha
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.bodyLarge
import com.artemchep.keyguard.ui.theme.labelLarge

@Composable
fun FilterSectionComposable(
    modifier: Modifier = Modifier,
    expandable: Boolean,
    expanded: Boolean,
    title: String,
    onClick: (() -> Unit)?,
) {
    BasicComponent(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.combinedClickable(
                        onClick = onClick ?: {},
                    )
                } else {
                    Modifier
                },
            ),
        endActions = {
            if (!expandable) {
                return@endActions
            }

            val targetRotationX =
                if (expanded) {
                    0f
                } else {
                    180f
                }
            val rotationX by animateFloatAsState(targetRotationX)
            Icon(
                modifier = Modifier
                    .graphicsLayer(
                        rotationX = rotationX,
                        rotationZ = -90f,
                    ),
                tint = LocalContentColor.current
                    .combineAlpha(MediumEmphasisAlpha),
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
            )
        },
        insideMargin = PaddingValues(
            horizontal = 8.dp,
            vertical = 0.dp,
        ),
        content = {
            CompositionLocalProvider(
                LocalTextStyle provides MiuixTheme.textStyles.bodyLarge,
            ) {
                Text(
                    text = title.uppercase(),
                    style = MiuixTheme.textStyles.labelLarge,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = LocalContentColor.current
                        .combineAlpha(MediumEmphasisAlpha),
                )
            }
        },
    )
}
