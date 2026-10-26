package com.lukeneedham.videodiary.ui.navigation.normal

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.lukeneedham.common.logger.Logger
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.common.hub.HubSwitcherSheet
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import kotlinx.coroutines.launch

/**
 * Owns the back stack and the [NavDisplay] of the main app flow, and hosts the hub switcher sheet around it.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppNavigation(
    share: (ShareRequest) -> Unit,
) {
    val backStack = rememberNavBackStack(Route.Calendar)

    val hubSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val openHubSwitcher: () -> Unit = {
        coroutineScope.launch { hubSheetState.show() }
    }

    // The hub switcher is only reachable from hub pages, so the top of the back stack is a hub
    // root page whenever it is open.
    val currentHub = (backStack.lastOrNull() as? Route)?.hub

    // The sheet is hosted here, outside the pages. When a hub is chosen the page behind it is
    // switched first, and the sheet then animates closed over the new page.
    fun switchToHub(hub: Hub) {
        // Read the live back stack rather than the composed currentHub, which can be stale by the
        // time the click lands
        val liveHub = (backStack.lastOrNull() as? Route)?.hub
        if (hub != liveHub) {
            Logger.d("Switching to hub: $hub")
            // The calendar hub is always the bottom entry of the back stack and is never removed,
            // so it keeps its state. Switching hub drops everything above it, then opens the new
            // hub on top. Back from another hub's root page returns to the calendar.
            backStack.popUpTo { it is Route.Calendar }
            when (hub) {
                Hub.Calendar -> Unit
                Hub.Recap -> backStack.add(Route.RecapHub)
                Hub.Settings -> backStack.add(Route.SettingsHub)
            }
        }
        coroutineScope.launch {
            // Wait for the new page to be composed and drawn before closing the sheet
            withFrameNanos { }
            withFrameNanos { }
            hubSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = hubSheetState,
        sheetBackgroundColor = SheetDefaults.containerColor,
        sheetShape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        sheetContent = {
            HubSwitcherSheet(
                currentHub = currentHub,
                onHubClick = ::switchToHub,
                onClose = { coroutineScope.launch { hubSheetState.hide() } },
            )
        },
    ) {
        // Pages swap without an animation
        val noTransition = EnterTransition.None togetherWith ExitTransition.None
        NavDisplay(
            backStack = backStack,
            // Each page has a view model of its own, kept for as long as the page is on the back stack
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            transitionSpec = { noTransition },
            popTransitionSpec = { noTransition },
            predictivePopTransitionSpec = { noTransition },
            entryProvider = { key -> routeEntryProvider(backStack, key, share, openHubSwitcher) },
        )
    }

    // Composed after the NavDisplay, so that this takes priority over its back handling while the sheet is open
    BackHandler(enabled = hubSheetState.isVisible) {
        coroutineScope.launch { hubSheetState.hide() }
    }
}

private fun routeEntryProvider(
    backStack: NavBackStack<NavKey>,
    key: NavKey,
    share: (ShareRequest) -> Unit,
    openHubSwitcher: () -> Unit,
): NavEntry<NavKey> {
    val route = key as Route
    return NavEntry(route) {
        Router(backStack, route, share, openHubSwitcher)
    }
}

/** Pops entries until the top of the back stack satisfies [predicate]; does nothing if no entry does. */
internal fun NavBackStack<NavKey>.popUpTo(predicate: (NavKey) -> Boolean) {
    val index = indexOfLast(predicate)
    if (index < 0) return
    while (lastIndex > index) removeLastOrNull()
}
