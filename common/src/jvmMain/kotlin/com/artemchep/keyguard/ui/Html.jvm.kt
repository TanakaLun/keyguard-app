package com.artemchep.keyguard.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
actual fun HtmlText(
    modifier: Modifier,
    html: String,
) {
    de.charlex.compose.htmltext.material3.HtmlText(
        modifier = modifier,
        urlSpanStyle = SpanStyle(
            color = MiuixTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
        ),
        text = html,
    )
}
