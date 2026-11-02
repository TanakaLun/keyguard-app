package com.artemchep.keyguard.feature.watchtower

import androidx.compose.runtime.Composable
import com.artemchep.keyguard.common.model.DFilter
import com.artemchep.keyguard.feature.navigation.Route
import com.artemchep.keyguard.feature.navigation.RouteDescriptor

data class WatchtowerRoute(
    val args: Args = Args(),
) : Route {
    override val descriptor
        get() = RouteDescriptor.Watchtower(
            filter = args.filter,
        )

    data class Args(
        val filter: DFilter? = null,
    )

    @Composable
    override fun Content() {
        WatchtowerScreen(
            args = args,
        )
    }
}
