package com.artemchep.keyguard.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemchep.keyguard.ui.theme.Dimens
import top.yukonga.miuix.kmp.basic.BreadcrumbBar
import top.yukonga.miuix.kmp.basic.BreadcrumbItem

/** A node of a folder path; the current folder has no [onClick]. */
data class Breadcrumb(
    val name: String,
    val onClick: (() -> Unit)?,
)

/** Builds the breadcrumbs of a `/`-separated [path] relative to the root. */
fun pathBreadcrumbs(
    path: String,
    rootName: String = "/",
    onClick: (String) -> Unit,
): List<Breadcrumb> {
    val parts = path
        .split('/')
        .filter { part -> part.isNotEmpty() }
    val paths = parts.runningFold("") { parent, part ->
        if (parent.isEmpty()) part else "$parent/$part"
    }
    return paths.mapIndexed { index, itemPath ->
        Breadcrumb(
            name = if (index == 0) rootName else parts[index - 1],
            onClick = if (index == paths.lastIndex) {
                null
            } else {
                {
                    onClick(itemPath)
                }
            },
        )
    }
}

/** The folder path as a list header. */
fun LazyListScope.breadcrumbsItem(
    breadcrumbs: List<Breadcrumb>,
) {
    item("breadcrumbs") {
        BreadcrumbTree(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.contentPadding),
            breadcrumbs = breadcrumbs,
        )
    }
    item("breadcrumbs.spacer") {
        // Spacer kept for visual rhythm.
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )
    }
}

/** Parent folders as chips, followed by the current folder. */
@Composable
fun BreadcrumbTree(
    breadcrumbs: List<Breadcrumb>,
    modifier: Modifier = Modifier,
) {
    BreadcrumbBar(
        modifier = modifier,
        items = breadcrumbs.map { breadcrumb ->
            BreadcrumbItem(
                path = breadcrumb.name,
            )
        },
        onItemClick = { index ->
            // The current (last) folder has no onClick and is not clickable.
            breadcrumbs.getOrNull(index)?.onClick?.invoke()
        },
    )
}