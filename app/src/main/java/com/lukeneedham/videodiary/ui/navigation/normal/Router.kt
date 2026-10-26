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
import com.lukeneedham.videodiary.ui.feature.settings.SettingsHubPage

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
            onRecordVideoClick = { date ->
                navigateTo(Route.RecordVideo(date))
            },
            onMenuClick = openHubSwitcher,
            share = share,
        )

        is Route.RecordVideo -> RecordVideoPage(
            date = route.date,
            onCheckVideo = { videoContentUri ->
                navigateTo(
                    Route.CheckVideo(
                        date = route.date,
                        videoContentUri = videoContentUri,
                    )
                )
            },
            onVideoSaved = ::popUpToCalendar,
            onBack = ::onBack,
        )

        is Route.CheckVideo -> CheckVideoPage(
            date = route.date,
            videoContentUri = route.videoContentUri,
            onRetake = ::onBack,
            onKeepExisting = ::popUpToCalendar,
            onKeepNew = ::popUpToCalendar,
        )

        Route.RecapHub -> RecapHubPage(
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
            periodType = route.periodType,
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
            startDate = route.startDate,
            endDate = route.endDate,
            name = route.name,
            savedRecapId = route.savedRecapId,
            canGoBack = canGoBack,
            onBack = ::onBack,
            share = share,
        )

        Route.SettingsHub -> SettingsHubPage(
            onMenuClick = openHubSwitcher,
            onDebugClick = {
                navigateTo(Route.AppDebug)
            },
        )

        // The app's own debug page, which opens the shared library's debug page. The library's
        // debug, logs and crashes pages show only the library's own things.
        Route.AppDebug -> AppDebugPage(
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
