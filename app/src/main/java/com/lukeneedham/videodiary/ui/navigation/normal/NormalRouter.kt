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
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoViewModel
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NormalRouter(
    share: (ShareRequest) -> Unit,
) {
    val navController = rememberNavController<AppNavKey>(startDestination = CalendarNavKey)

    val canGoBack = navController.backstack.entries.size > 1

    fun navigate(to: AppNavKey) {
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
    val currentHub = (navController.backstack.entries.lastOrNull()?.destination as? AppNavKey)
        ?.hub

    // The sheet is hosted here, outside the pages. When a hub is chosen the page behind it is
    // switched first, and the sheet then animates closed over the new page.
    fun switchToHub(hub: Hub) {
        // Read the live backstack rather than the composed currentHub, which can be stale by the
        // time the click lands
        val liveHub = (navController.backstack.entries.lastOrNull()?.destination as? AppNavKey)?.hub
        if (hub != liveHub) {
            Logger.d("Switching to hub: $hub")
            // The calendar hub is always the bottom entry of the backstack and is never removed,
            // so it keeps its state. Switching hub drops everything above it, then opens the new
            // hub on top. Back from another hub's root page returns to the calendar.
            navController.popUpTo { it is CalendarNavKey }
            when (hub) {
                Hub.Calendar -> Unit
                Hub.Recap -> navController.navigate(RecapHubNavKey)
                Hub.Settings -> navController.navigate(SettingsHubNavKey)
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
                is CalendarNavKey -> CalendarPage(
                    viewModel = koinViewModel(),
                    onRecordVideoClick = { date ->
                        navigate(RecordVideoNavKey(date))
                    },
                    onMenuClick = openHubSwitcher,
                    share = share,
                )

                is RecordVideoNavKey -> {
                    val viewModel = koinViewModel<RecordVideoViewModel> {
                        parametersOf(page.date)
                    }
                    RecordVideoPage(
                        viewModel = viewModel,
                        onRecordingFinished = { videoContentUri ->
                            if (viewModel.hasExistingVideo) {
                                navigate(
                                    CheckVideoNavKey(
                                        date = page.date,
                                        videoContentUri = videoContentUri,
                                    )
                                )
                            } else {
                                viewModel.persistVideoDirectly(videoContentUri)
                                navController.popUpTo { it is CalendarNavKey }
                            }
                        },
                        onBack = onBack,
                    )
                }

                is CheckVideoNavKey -> {
                    val returnToCalendar: () -> Unit = {
                        navController.popUpTo { it is CalendarNavKey }
                    }
                    CheckVideoPage(
                        viewModel = koinViewModel {
                            parametersOf(page.date, page.videoContentUri)
                        },
                        onRetake = {
                            pop()
                        },
                        onKeepExisting = returnToCalendar,
                        onKeepNew = returnToCalendar,
                    )
                }

                is RecapHubNavKey -> RecapHubPage(
                    viewModel = koinViewModel(),
                    onMenuClick = openHubSwitcher,
                    onCreateWeekClick = {
                        navigate(RecapCreatePeriodListNavKey(RecapPeriodType.WEEK))
                    },
                    onCreateMonthClick = {
                        navigate(RecapCreatePeriodListNavKey(RecapPeriodType.MONTH))
                    },
                    onCreateYearClick = {
                        navigate(RecapCreatePeriodListNavKey(RecapPeriodType.YEAR))
                    },
                    onCreateCustomClick = {
                        navigate(RecapCreateCustomNavKey)
                    },
                    onRecapClick = { savedRecap ->
                        navigate(
                            RecapViewNavKey(
                                startDate = savedRecap.startDate,
                                endDate = savedRecap.endDate,
                                name = savedRecap.name,
                                savedRecapId = savedRecap.id,
                            )
                        )
                    },
                )

                is RecapCreatePeriodListNavKey -> RecapCreatePeriodListPage(
                    viewModel = koinViewModel {
                        parametersOf(page.periodType)
                    },
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onOptionClick = { option ->
                        navigate(
                            RecapViewNavKey(
                                startDate = option.startDate,
                                endDate = option.endDate,
                                name = option.suggestedName,
                                savedRecapId = null,
                            )
                        )
                    },
                )

                is RecapCreateCustomNavKey -> RecapCreateCustomPage(
                    viewModel = koinViewModel(),
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onRecapCreated = { startDate, endDate, name, savedRecapId ->
                        navController.popUpTo { it is RecapHubNavKey }
                        navigate(
                            RecapViewNavKey(
                                startDate = startDate,
                                endDate = endDate,
                                name = name,
                                savedRecapId = savedRecapId,
                            )
                        )
                    },
                )

                is RecapViewNavKey -> RecapViewPage(
                    viewModel = koinViewModel {
                        parametersOf(page.startDate, page.endDate, page.name, page.savedRecapId)
                    },
                    canGoBack = canGoBack,
                    onBack = onBack,
                    share = share,
                )

                is SettingsHubNavKey -> SettingsHubPage(
                    onMenuClick = openHubSwitcher,
                    onDebugClick = {
                        navigate(AppDebugNavKey)
                    },
                )

                // The app's own debug page, which opens the shared library's debug page. The library's
                // debug, logs and crashes pages show only the library's own things.
                is AppDebugNavKey -> AppDebugPage(
                    viewModel = koinViewModel(),
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onOpenSdkDebug = { navigate(DebugNavKey) },
                )

                is DebugNavKey -> DebugPage(
                    onBack = onBack,
                    onOpenLogs = { navigate(LogsNavKey) },
                    onOpenCrashes = { navigate(CrashesNavKey) },
                )

                is LogsNavKey -> LogsPage(onBack = onBack)

                is CrashesNavKey -> CrashesPage(onBack = onBack)
            }
        }
    }
}
