package com.artemchep.keyguard.feature.generator.wordlist

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import com.artemchep.keyguard.feature.generator.wordlist.list.WordlistListScreen
import com.artemchep.keyguard.feature.localization.wrap
import com.artemchep.keyguard.feature.navigation.NavigationIntent
import com.artemchep.keyguard.feature.navigation.Route
import com.artemchep.keyguard.feature.navigation.RouteDescriptor
import com.artemchep.keyguard.feature.navigation.state.TranslatorScope
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.res.*
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.icons.ChevronIcon
import com.artemchep.keyguard.ui.icons.KeyguardWordlist
import com.artemchep.keyguard.ui.icons.iconSmall

object WordlistsRoute : Route {
    override val descriptor
        get() = RouteDescriptor.WordlistList

    fun actionOrNull(
        translator: TranslatorScope,
        navigate: (NavigationIntent) -> Unit,
    ) = action(
        translator = translator,
        navigate = navigate,
    )

    fun action(
        translator: TranslatorScope,
        navigate: (NavigationIntent) -> Unit,
    ) = FlatItemAction(
        id = "wordlist.list",
        leading = iconSmall(Icons.Outlined.KeyguardWordlist),
        title = Res.string.wordlist_list_header_title.wrap(),
        trailing = {
            ChevronIcon()
        },
        onClick = {
            val route = WordlistsRoute
            val intent = NavigationIntent.NavigateToRoute(route)
            navigate(intent)
        },
    )

    @Composable
    override fun Content() {
        WordlistListScreen()
    }
}
