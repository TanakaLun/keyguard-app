package com.artemchep.keyguard.feature.home.settings.backups

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.ui.MediumEmphasisAlpha
import com.artemchep.keyguard.ui.theme.Dimens
import com.artemchep.keyguard.ui.theme.combineAlpha
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.artemchep.keyguard.ui.theme.titleSmall

internal fun LazyListScope.automaticBackupsSettingsHeader(
    key: String,
    title: String,
) {
    item("$key.title") {
        Text(
            modifier = Modifier
                .animateItem()
                .padding(
                    start = Dimens.textHorizontalPadding,
                    end = Dimens.textHorizontalPadding,
                    top = 24.dp,
                    bottom = 8.dp,
                ),
            text = title,
            style = MiuixTheme.textStyles.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MiuixTheme.colorScheme.onSurface.combineAlpha(MediumEmphasisAlpha),
        )
    }
}
