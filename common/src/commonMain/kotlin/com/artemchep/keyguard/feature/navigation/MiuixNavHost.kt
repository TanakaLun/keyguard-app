package com.artemchep.keyguard.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.artemchep.keyguard.ui.LocalIsStackRootScreen
import com.artemchep.keyguard.common.model.NavAnimation
import com.artemchep.keyguard.common.usecase.GetNavAnimation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import org.koin.compose.koinInject
import top.yukonga.miuix.kmp.nav.core.NavDisplay
import top.yukonga.miuix.kmp.nav.core.NavKey
import top.yukonga.miuix.kmp.nav.transition.NavSwipeDirection
import top.yukonga.miuix.kmp.nav.transition.NavTransition
import top.yukonga.miuix.kmp.nav.transition.NavTransitions
import top.yukonga.miuix.kmp.nav.transition.navGraphicsTransition

/**
 * A miuix navigation key that wraps a keyguard [NavigationEntry].
 *
 * Equality is instance based: the entry instance is the unit of route identity in
 * keyguard, and a re-created entry (a new route for the same id) must be
 * re-registered with [NavDisplay] so the content renders the new route.
 */
@Immutable
private class KgNavKey(
    val entry: NavigationEntry,
) : NavKey {
    override fun equals(other: Any?): Boolean = other is KgNavKey && other.entry === entry

    override fun hashCode(): Int = entry.hashCode()

    override fun toString(): String = "kg:${entry.id}"
}

/**
 * The stack slices an entry renders with, relative to the node that hosts it.
 */
@Immutable
private data class EntryStacks(
    val logical: PersistentList<NavigationEntry>,
    val visual: PersistentList<NavigationEntry>,
)

/**
 * Fades the entering page over the covered one instead of sliding it in.
 */
private val CrossfadeNavTransition: NavTransition = navGraphicsTransition(
    opaqueDepth = 1f,
) { scope ->
    val depth = scope.relativeDepth
    if (depth <= 0f) {
        alpha = (1f + depth).coerceIn(0f, 1f)
    }
}

/**
 * Renders the fullscreen part of a navigation stack with the standard miuix
 * navigation runtime, which owns the transitions, the predictive back gesture
 * and the interactive swipe-to-dismiss.
 *
 * The keyguard stack stays the source of truth: it is projected onto the miuix
 * back stack on every composition, so navigation intents, persistence and
 * back-press interception keep working exactly as before.
 *
 * @param entries all entries of the hosting node.
 * @param visualEntries [entries] without the leading [offset].
 * @param fullscreenCount how many leading entries of [visualEntries] are
 *   fullscreen routes (the rest are dialog routes rendered by the caller).
 * @param offset how many entries of [entries] were skipped by the node.
 */
@Composable
internal fun MiuixNavHost(
    entries: PersistentList<NavigationEntry>,
    visualEntries: ImmutableList<NavigationEntry>,
    fullscreenCount: Int,
    offset: Int,
    modifier: Modifier = Modifier,
) {
    val getNavAnimation = koinInject<GetNavAnimation>()
    val controller = LocalNavigationController.current

    val baseLogicalStack = LocalNavigationNodeLogicalStack.current
    val baseVisualStack = LocalNavigationNodeVisualStack.current
    val parentFinishing = LocalNavigationNodeFinishing.current

    val stacksById = remember(
        entries,
        visualEntries,
        fullscreenCount,
        offset,
        baseLogicalStack,
        baseVisualStack,
    ) {
        buildMap<String, EntryStacks>(fullscreenCount) {
            for (i in 0 until fullscreenCount) {
                val entry = visualEntries[i]
                put(
                    entry.id,
                    EntryStacks(
                        logical = baseLogicalStack.addAll(entries.subList(0, i + offset + 1)),
                        visual = baseVisualStack.addAll(visualEntries.subList(0, i + 1)),
                    ),
                )
            }
        }
    }
    // Entries that are animating out are no longer part of the stack above, but
    // they still need the stack they were rendered with.
    val lastStacks = remember { mutableMapOf<String, EntryStacks>() }
    lastStacks.putAll(stacksById)

    val backStack = remember {
        mutableStateListOf<NavKey>().apply {
            for (i in 0 until fullscreenCount) {
                add(KgNavKey(visualEntries[i]))
            }
        }
    }
    // Never mutate snapshot state while composing: the projection is applied
    // after the composition instead.
    SideEffect {
        backStack.syncWith(
            visualEntries = visualEntries,
            fullscreenCount = fullscreenCount,
        )
    }

    if (backStack.isEmpty()) {
        Box(modifier)
        return
    }

    // A root screen has nothing to animate, so it renders directly. This also
    // keeps the tab roots free of the navigation runtime.
    if (fullscreenCount == 1) {
        val entry = visualEntries[0]
        val stacks = stacksById[entry.id]
        Box(modifier) {
            CompositionLocalProvider(
                LocalNavigationNodeLogicalStack provides (stacks?.logical ?: baseLogicalStack),
                LocalNavigationNodeVisualStack provides (stacks?.visual ?: baseVisualStack),
                LocalNavigationNodeFinishing provides parentFinishing,
                LocalIsStackRootScreen provides true,
            ) {
                NavigationRoute(
                    entry = entry,
                )
            }
        }
        return
    }

    val transition: NavTransition = when (getNavAnimation().value) {
        NavAnimation.DISABLED -> NavTransitions.None
        NavAnimation.CROSSFADE -> CrossfadeNavTransition
        NavAnimation.DYNAMIC -> NavTransitions.MiuixDefault
    }
    val swipeDismiss = if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
        NavSwipeDirection.RightToLeft
    } else {
        NavSwipeDirection.LeftToRight
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = {
            controller.queue(NavigationIntent.Pop)
        },
        transition = transition,
    ) {
        entry<KgNavKey>(
            contentKey = { it.entry.id },
            swipeDismiss = swipeDismiss,
        ) { key ->
            val entry = key.entry
            val stacks = stacksById[entry.id] ?: lastStacks[entry.id]
            val isRoot = visualEntries.indexOfFirst { it.id == entry.id } == 0
            CompositionLocalProvider(
                LocalNavigationNodeLogicalStack provides (stacks?.logical ?: baseLogicalStack),
                LocalNavigationNodeVisualStack provides (stacks?.visual ?: baseVisualStack),
                LocalNavigationNodeFinishing provides (parentFinishing || entry.id !in stacksById),
                LocalIsStackRootScreen provides isRoot,
            ) {
                NavigationRoute(
                    entry = entry,
                )
            }
        }
    }
}

/**
 * Projects the keyguard fullscreen stack onto the miuix back stack.
 *
 * The update is incremental so that miuix can animate the pushed/popped entry
 * instead of re-creating the whole stack: a shared prefix of entry ids is kept
 * as is, the tail is dropped and re-appended.
 */
private fun MutableList<NavKey>.syncWith(
    visualEntries: ImmutableList<NavigationEntry>,
    fullscreenCount: Int,
) {
    var prefix = 0
    while (
        prefix < size &&
        prefix < fullscreenCount &&
        (this[prefix] as? KgNavKey)?.entry?.id == visualEntries[prefix].id
    ) {
        prefix++
    }

    while (size > prefix) {
        removeAt(lastIndex)
    }
    for (i in prefix until fullscreenCount) {
        add(KgNavKey(visualEntries[i]))
    }

    // A re-created entry keeps its position but must be re-registered so the
    // content picks up the new route.
    for (i in 0 until prefix) {
        val entry = visualEntries[i]
        if ((this[i] as? KgNavKey)?.entry !== entry) {
            this[i] = KgNavKey(entry)
        }
    }
}
