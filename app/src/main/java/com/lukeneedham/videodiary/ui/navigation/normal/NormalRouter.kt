package com.lukeneedham.videodiary.ui.navigation.normal

import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import com.lukeneedham.common.logger.Logger
import com.lukeneedham.common.ui.CrashesPage
import com.lukeneedham.common.ui.DebugPage
import com.lukeneedham.common.ui.LogsPage
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.common.hub.HubSwitcherSheet
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.debug.AppDebugPage
import com.lukeneedham.videodiary.ui.feature.settings.SettingsHubPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateCustomPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreatePeriodListPage
import com.lukeneedham.videodiary.ui.feature.recap.hub.RecapHubPage
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import com.lukeneedham.videodiary.ui.feature.recap.view.RecapViewPage
import com.lukeneedham.videodiary.ui.feature.record.check.CheckVideoPage
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoPage
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NormalRouter(
    share: (ShareRequest) -> Unit,
) {
    val navController = rememberNavController<Route>(startDestination = Route.Calendar)

    val canGoBack = navController.backstack.entries.size > 1

    fun navigate(to: Route) {
        Logger.d("Navigating to: $to")
        navController.navigate(to)
    }

    fun pop() {
        if (!canGoBack) return
        navController.pop()
    }

    val onBack = ::pop

    val hubSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val openHubSwitcher: () -> Unit = {
        coroutineScope.launch { hubSheetState.show() }
    }

    // The hub switcher is only reachable from hub pages, so the top of the backstack is a hub
    // root page whenever it is open.
    val currentHub = (navController.backstack.entries.lastOrNull()?.destination as? Route)
        ?.hub

    // The sheet is hosted here, outside the pages. When a hub is chosen the page behind it is
    // switched first, and the sheet then animates closed over the new page.
    fun switchToHub(hub: Hub) {
        // Read the live backstack rather than the composed currentHub, which can be stale by the
        // time the click lands
        val liveHub = (navController.backstack.entries.lastOrNull()?.destination as? Route)?.hub
        if (hub != liveHub) {
            Logger.d("Switching to hub: $hub")
            // The calendar hub is always the bottom entry of the backstack and is never removed,
            // so it keeps its state. Switching hub drops everything above it, then opens the new
            // hub on top. Back from another hub's root page returns to the calendar.
            navController.popUpTo { it is Route.Calendar }
            when (hub) {
                Hub.Calendar -> Unit
                Hub.Recap -> navController.navigate(Route.RecapHub)
                Hub.Settings -> navController.navigate(Route.SettingsHub)
            }
        }
        coroutineScope.launch {
            // Wait for the new page to be composed and drawn before closing the sheet
            withFrameNanos { }
            withFrameNanos { }
            hubSheetState.hide()
        }
    }

    NavBackHandler(navController)

    // Registered after the NavBackHandler so that it takes priority while the sheet is open
    BackHandler(enabled = hubSheetState.isVisible) {
        coroutineScope.launch { hubSheetState.hide() }
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
        NavHost(
            controller = navController,
        ) { page ->
            when (page) {
                is Route.Calendar -> CalendarPage(
                    onRecordVideoClick = { date ->
                        navigate(Route.RecordVideo(date))
                    },
                    onMenuClick = openHubSwitcher,
                    share = share,
                )

                is Route.RecordVideo -> RecordVideoPage(
                    date = page.date,
                    onCheckVideo = { videoContentUri ->
                        navigate(
                            Route.CheckVideo(
                                date = page.date,
                                videoContentUri = videoContentUri,
                            )
                        )
                    },
                    onVideoSaved = {
                        navController.popUpTo { it is Route.Calendar }
                    },
                    onBack = onBack,
                )

                is Route.CheckVideo -> {
                    val returnToCalendar: () -> Unit = {
                        navController.popUpTo { it is Route.Calendar }
                    }
                    CheckVideoPage(
                        date = page.date,
                        videoContentUri = page.videoContentUri,
                        onRetake = {
                            pop()
                        },
                        onKeepExisting = returnToCalendar,
                        onKeepNew = returnToCalendar,
                    )
                }

                is Route.RecapHub -> RecapHubPage(
                    onMenuClick = openHubSwitcher,
                    onCreateWeekClick = {
                        navigate(Route.RecapCreatePeriodList(RecapPeriodType.WEEK))
                    },
                    onCreateMonthClick = {
                        navigate(Route.RecapCreatePeriodList(RecapPeriodType.MONTH))
                    },
                    onCreateYearClick = {
                        navigate(Route.RecapCreatePeriodList(RecapPeriodType.YEAR))
                    },
                    onCreateCustomClick = {
                        navigate(Route.RecapCreateCustom)
                    },
                    onRecapClick = { savedRecap ->
                        navigate(
                            Route.RecapView(
                                startDate = savedRecap.startDate,
                                endDate = savedRecap.endDate,
                                name = savedRecap.name,
                                savedRecapId = savedRecap.id,
                            )
                        )
                    },
                )

                is Route.RecapCreatePeriodList -> RecapCreatePeriodListPage(
                    periodType = page.periodType,
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onOptionClick = { option ->
                        navigate(
                            Route.RecapView(
                                startDate = option.startDate,
                                endDate = option.endDate,
                                name = option.suggestedName,
                                savedRecapId = null,
                            )
                        )
                    },
                )

                is Route.RecapCreateCustom -> RecapCreateCustomPage(
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onRecapCreated = { startDate, endDate, name, savedRecapId ->
                        navController.popUpTo { it is Route.RecapHub }
                        navigate(
                            Route.RecapView(
                                startDate = startDate,
                                endDate = endDate,
                                name = name,
                                savedRecapId = savedRecapId,
                            )
                        )
                    },
                )

                is Route.RecapView -> RecapViewPage(
                    startDate = page.startDate,
                    endDate = page.endDate,
                    name = page.name,
                    savedRecapId = page.savedRecapId,
                    canGoBack = canGoBack,
                    onBack = onBack,
                    share = share,
                )

                is Route.SettingsHub -> SettingsHubPage(
                    onMenuClick = openHubSwitcher,
                    onDebugClick = {
                        navigate(Route.AppDebug)
                    },
                )

                // The app's own debug page, which opens the shared library's debug page. The library's
                // debug, logs and crashes pages show only the library's own things.
                is Route.AppDebug -> AppDebugPage(
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onOpenSdkDebug = { navigate(Route.Debug) },
                )

                is Route.Debug -> DebugPage(
                    onBack = onBack,
                    onOpenLogs = { navigate(Route.Logs) },
                    onOpenCrashes = { navigate(Route.Crashes) },
                )

                is Route.Logs -> LogsPage(onBack = onBack)

                is Route.Crashes -> CrashesPage(onBack = onBack)
            }
        }
    }
}
