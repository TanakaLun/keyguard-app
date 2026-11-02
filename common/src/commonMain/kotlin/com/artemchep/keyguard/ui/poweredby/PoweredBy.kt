package com.artemchep.keyguard.ui.poweredby

import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.theme.LocalContentColor
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.URL_2FA
import com.artemchep.keyguard.URL_HAVE_I_BEEN_PWNED
import com.artemchep.keyguard.URL_JUST_DELETE_ME
import com.artemchep.keyguard.URL_JUST_GET_MY_DATA
import com.artemchep.keyguard.URL_PASSKEYS
import com.artemchep.keyguard.feature.navigation.LocalNavigationController
import com.artemchep.keyguard.feature.navigation.NavigationIntent
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.DisabledEmphasisAlpha
import com.artemchep.keyguard.ui.theme.combineAlpha
import org.jetbrains.compose.resources.stringResource
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.labelSmall

@Composable
fun PoweredByJustDeleteMe(
    modifier: Modifier = Modifier,
) {
    PoweredByLabel(
        modifier = modifier,
        domain = "JustDeleteMe",
        url = URL_JUST_DELETE_ME,
    )
}

@Composable
fun PoweredByJustGetMyData(
    modifier: Modifier = Modifier,
) {
    PoweredByLabel(
        modifier = modifier,
        domain = "JustGetMyData",
        url = URL_JUST_GET_MY_DATA,
    )
}

@Composable
fun PoweredByHaveibeenpwned(
    modifier: Modifier = Modifier,
) {
    PoweredByLabel(
        modifier = modifier,
        domain = "HIBP",
        url = URL_HAVE_I_BEEN_PWNED,
    )
}

@Composable
fun PoweredBy2factorauth(
    modifier: Modifier = Modifier,
) {
    PoweredByLabel(
        modifier = modifier,
        domain = "2factorauth",
        url = URL_2FA,
    )
}

@Composable
fun PoweredByPasskeys(
    modifier: Modifier = Modifier,
) {
    PoweredByLabel(
        modifier = modifier,
        domain = "passkeys",
        url = URL_PASSKEYS,
    )
}

/**
 * The attribution line, e.g. "Powered by haveibeenpwned.com". The provider is
 * a hyperlink inside the sentence, not a separate button.
 */
@Composable
fun PoweredByLabel(
    modifier: Modifier = Modifier,
    domain: String,
    url: String,
) {
    val navigationController by rememberUpdatedState(LocalNavigationController.current)
    val updatedUrl by rememberUpdatedState(url)
    val text = stringResource(Res.string.powered_by_text, domain)
    val annotatedText = remember(text, domain, updatedUrl) {
        buildAnnotatedString {
            val start = text.indexOf(domain)
            if (start < 0) {
                append(text)
                return@buildAnnotatedString
            }
            append(text.substring(0, start))
            withLink(
                LinkAnnotation.Clickable(
                    tag = domain,
                    styles = TextLinkStyles(),
                    linkInteractionListener = {
                        val intent = NavigationIntent.NavigateToBrowser(
                            url = updatedUrl,
                        )
                        navigationController.queue(intent)
                    },
                ),
            ) {
                append(domain)
            }
            append(text.substring(start + domain.length))
        }
    }
    Text(
        modifier = modifier
            .padding(top = 4.dp),
        text = annotatedText,
        maxLines = 2,
        style = MiuixTheme.textStyles.labelSmall,
        color = LocalContentColor.current
            .combineAlpha(DisabledEmphasisAlpha),
    )
}
