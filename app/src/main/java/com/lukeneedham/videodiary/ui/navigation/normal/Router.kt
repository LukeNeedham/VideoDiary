package com.lukeneedham.videodiary.ui.navigation.normal

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.lukeneedham.common.logger.Logger
import com.lukeneedham.common.ui.CrashesPage
import com.lukeneedham.common.ui.DebugPage
import com.lukeneedham.common.ui.LogsPage
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.debug.AppDebugPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateCustomPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreatePeriodListPage
import com.lukeneedham.videodiary.ui.feature.recap.hub.RecapHubPage
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import com.lukeneedham.videodiary.ui.feature.recap.view.RecapViewPage
import com.lukeneedham.videodiary.ui.feature.record.check.CheckVideoPage
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoPage
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoViewModel
import com.lukeneedham.videodiary.ui.feature.settings.SettingsHubPage
import com.lukeneedham.videodiary.ui.feature.storage.VideoStoragePage
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/** Calls the page of [route], with the navigation as lambdas. The only place that calls a page. */
@Composable
fun Router(
    backStack: NavBackStack<NavKey>,
    route: Route,
    share: (ShareRequest) -> Unit,
    openHubSwitcher: () -> Unit,
) {
    // The common actions are defined once here, never repeated in each branch
    val canGoBack = backStack.size > 1

    fun navigateTo(target: Route) {
        Logger.d("Navigating to: $target")
        backStack.add(target)
    }

    fun onBack() {
        if (!canGoBack) return
        backStack.removeLastOrNull()
    }

    fun popUpToCalendar() = backStack.popUpTo { it is Route.Calendar }

    when (route) { // exhaustive: no `else`
        Route.Calendar -> CalendarPage(
            viewModel = koinViewModel(),
            onRecordVideoClick = { date ->
                navigateTo(Route.RecordVideo(date))
            },
            onMenuClick = openHubSwitcher,
            share = share,
        )

        is Route.RecordVideo -> {
            val viewModel = koinViewModel<RecordVideoViewModel> {
                parametersOf(route.date)
            }
            RecordVideoPage(
                viewModel = viewModel,
                onRecordingFinished = { videoContentUri ->
                    if (viewModel.hasExistingVideo) {
                        navigateTo(
                            Route.CheckVideo(
                                date = route.date,
                                videoContentUri = videoContentUri,
                            )
                        )
                    } else {
                        viewModel.persistVideoDirectly(videoContentUri)
                        popUpToCalendar()
                    }
                },
                onBack = ::onBack,
            )
        }

        is Route.CheckVideo -> CheckVideoPage(
            viewModel = koinViewModel {
                parametersOf(route.date, route.videoContentUri)
            },
            onRetake = ::onBack,
            onKeepExisting = ::popUpToCalendar,
            onKeepNew = ::popUpToCalendar,
        )

        Route.RecapHub -> RecapHubPage(
            viewModel = koinViewModel(),
            onMenuClick = openHubSwitcher,
            onCreateWeekClick = {
                navigateTo(Route.RecapCreatePeriodList(RecapPeriodType.WEEK))
            },
            onCreateMonthClick = {
                navigateTo(Route.RecapCreatePeriodList(RecapPeriodType.MONTH))
            },
            onCreateYearClick = {
                navigateTo(Route.RecapCreatePeriodList(RecapPeriodType.YEAR))
            },
            onCreateCustomClick = {
                navigateTo(Route.RecapCreateCustom)
            },
            onRecapClick = { savedRecap ->
                navigateTo(
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
            viewModel = koinViewModel {
                parametersOf(route.periodType)
            },
            canGoBack = canGoBack,
            onBack = ::onBack,
            onOptionClick = { option ->
                navigateTo(
                    Route.RecapView(
                        startDate = option.startDate,
                        endDate = option.endDate,
                        name = option.suggestedName,
                        savedRecapId = null,
                    )
                )
            },
        )

        Route.RecapCreateCustom -> RecapCreateCustomPage(
            viewModel = koinViewModel(),
            canGoBack = canGoBack,
            onBack = ::onBack,
            onRecapCreated = { startDate, endDate, name, savedRecapId ->
                backStack.popUpTo { it is Route.RecapHub }
                navigateTo(
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
            viewModel = koinViewModel {
                parametersOf(route.startDate, route.endDate, route.name, route.savedRecapId)
            },
            canGoBack = canGoBack,
            onBack = ::onBack,
            share = share,
        )

        Route.SettingsHub -> SettingsHubPage(
            onMenuClick = openHubSwitcher,
            onVideoStorageClick = {
                navigateTo(Route.VideoStorage)
            },
            onDebugClick = {
                navigateTo(Route.AppDebug)
            },
            onLogsClick = {
                navigateTo(Route.Logs)
            },
            onCrashesClick = {
                navigateTo(Route.Crashes)
            },
        )

        Route.VideoStorage -> VideoStoragePage(
            viewModel = koinViewModel(),
            canGoBack = canGoBack,
            onBack = ::onBack,
        )

        // The app's own debug page, which opens the shared library's debug page. The library's
        // debug, logs and crashes pages show only the library's own things.
        Route.AppDebug -> AppDebugPage(
            viewModel = koinViewModel(),
            canGoBack = canGoBack,
            onBack = ::onBack,
            onOpenSdkDebug = { navigateTo(Route.Debug) },
        )

        Route.Debug -> DebugPage(
            onBack = ::onBack,
            onOpenLogs = { navigateTo(Route.Logs) },
            onOpenCrashes = { navigateTo(Route.Crashes) },
        )

        Route.Logs -> LogsPage(onBack = ::onBack)

        Route.Crashes -> CrashesPage(onBack = ::onBack)
    }
}
