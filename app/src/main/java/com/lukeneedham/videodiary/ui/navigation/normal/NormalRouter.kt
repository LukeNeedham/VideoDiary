package com.lukeneedham.videodiary.ui.navigation.normal

import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.common.hub.Hub
import com.lukeneedham.videodiary.ui.feature.common.hub.HubSwitcherSheet
import com.lukeneedham.videodiary.ui.feature.crashlog.CrashLogPage
import com.lukeneedham.videodiary.ui.feature.debug.DebugPage
import com.lukeneedham.videodiary.ui.feature.settings.SettingsHubPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateCustomPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreatePeriodListPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateTypePage
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
import com.lukeneedham.videodiary.ui.theme.AppSurface
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NormalRouter(
    share: (ShareRequest) -> Unit,
) {
    val navController = rememberNavController<NormalPage>(startDestination = NormalPage.Calendar)

    val canGoBack = navController.backstack.entries.size > 1

    fun navigate(to: NormalPage) {
        Logger.debug("Navigating to: $to")
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
    val currentHub = (navController.backstack.entries.lastOrNull()?.destination as? NormalPage)
        ?.hub

    // The sheet is hosted here, outside the pages. When a hub is chosen the page behind it is
    // switched first, and the sheet then animates closed over the new page.
    fun switchToHub(hub: Hub) {
        if (hub != currentHub) {
            Logger.debug("Switching to hub: $hub")
            // The calendar hub is always the bottom entry of the backstack and is never removed,
            // so it keeps its state. Switching hub drops everything above it, then opens the new
            // hub on top. Back from another hub's root page returns to the calendar.
            navController.popUpTo { it is NormalPage.Calendar }
            when (hub) {
                Hub.Calendar -> Unit
                Hub.Recap -> navController.navigate(NormalPage.RecapHub)
                Hub.Settings -> navController.navigate(NormalPage.SettingsHub)
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
        sheetBackgroundColor = AppSurface,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetContent = {
            HubSwitcherSheet(
                currentHub = currentHub,
                onHubClick = ::switchToHub,
            )
        },
    ) {
        NavHost(
            controller = navController,
        ) { page ->
            when (page) {
                is NormalPage.Calendar -> CalendarPage(
                    viewModel = koinViewModel(),
                    onRecordVideoClick = { date ->
                        navigate(NormalPage.RecordVideo(date))
                    },
                    onMenuClick = openHubSwitcher,
                    share = share,
                )

                is NormalPage.RecordVideo -> {
                    val viewModel = koinViewModel<RecordVideoViewModel> {
                        parametersOf(page.date)
                    }
                    RecordVideoPage(
                        viewModel = viewModel,
                        onRecordingFinished = { videoContentUri ->
                            if (viewModel.hasExistingVideo) {
                                navigate(
                                    NormalPage.CheckVideo(
                                        date = page.date,
                                        videoContentUri = videoContentUri,
                                    )
                                )
                            } else {
                                viewModel.persistVideoDirectly(videoContentUri)
                                navController.popUpTo { it is NormalPage.Calendar }
                            }
                        },
                        onBack = onBack,
                    )
                }

                is NormalPage.CheckVideo -> {
                    val returnToCalendar: () -> Unit = {
                        navController.popUpTo { it is NormalPage.Calendar }
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

                is NormalPage.RecapHub -> RecapHubPage(
                    viewModel = koinViewModel(),
                    onMenuClick = openHubSwitcher,
                    onCreateRecapClick = {
                        navigate(NormalPage.RecapCreateType)
                    },
                    onRecapClick = { savedRecap ->
                        navigate(
                            NormalPage.RecapView(
                                startDate = savedRecap.startDate,
                                endDate = savedRecap.endDate,
                                name = savedRecap.name,
                                savedRecapId = savedRecap.id,
                            )
                        )
                    },
                )

                is NormalPage.RecapCreateType -> RecapCreateTypePage(
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onCreateMonthClick = {
                        navigate(NormalPage.RecapCreatePeriodList(RecapPeriodType.MONTH))
                    },
                    onCreateWeekClick = {
                        navigate(NormalPage.RecapCreatePeriodList(RecapPeriodType.WEEK))
                    },
                    onCreateYearClick = {
                        navigate(NormalPage.RecapCreatePeriodList(RecapPeriodType.YEAR))
                    },
                    onCreateCustomClick = {
                        navigate(NormalPage.RecapCreateCustom)
                    },
                )

                is NormalPage.RecapCreatePeriodList -> RecapCreatePeriodListPage(
                    viewModel = koinViewModel {
                        parametersOf(page.periodType)
                    },
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onOptionClick = { option ->
                        navigate(
                            NormalPage.RecapView(
                                startDate = option.startDate,
                                endDate = option.endDate,
                                name = option.suggestedName,
                                savedRecapId = null,
                            )
                        )
                    },
                )

                is NormalPage.RecapCreateCustom -> RecapCreateCustomPage(
                    viewModel = koinViewModel(),
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onRecapCreated = { startDate, endDate, name, savedRecapId ->
                        navController.popUpTo { it is NormalPage.RecapHub }
                        navigate(
                            NormalPage.RecapView(
                                startDate = startDate,
                                endDate = endDate,
                                name = name,
                                savedRecapId = savedRecapId,
                            )
                        )
                    },
                )

                is NormalPage.RecapView -> RecapViewPage(
                    viewModel = koinViewModel {
                        parametersOf(page.startDate, page.endDate, page.name, page.savedRecapId)
                    },
                    canGoBack = canGoBack,
                    onBack = onBack,
                    share = share,
                )

                is NormalPage.SettingsHub -> SettingsHubPage(
                    onMenuClick = openHubSwitcher,
                    onDebugClick = {
                        navigate(NormalPage.Debug)
                    },
                )

                is NormalPage.Debug -> DebugPage(
                    viewModel = koinViewModel(),
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onCrashLogClick = {
                        navigate(NormalPage.CrashLog)
                    },
                )

                is NormalPage.CrashLog -> CrashLogPage(
                    viewModel = koinViewModel(),
                    canGoBack = canGoBack,
                    onBack = onBack,
                )
            }
        }
    }
}
