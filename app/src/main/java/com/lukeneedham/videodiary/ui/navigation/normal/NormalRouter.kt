package com.lukeneedham.videodiary.ui.navigation.normal

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.crashlog.CrashLogPage
import com.lukeneedham.videodiary.ui.feature.debug.DebugPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateCustomPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreatePeriodListPage
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateTypePage
import com.lukeneedham.videodiary.ui.feature.recap.export.RecapExportProgressPage
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
import org.koin.core.parameter.parametersOf

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

    NavBackHandler(navController)

    NavHost(
        controller = navController,
    ) { page ->
        when (page) {
            is NormalPage.Calendar -> CalendarPage(
                viewModel = koinViewModel(),
                onRecordVideoClick = { date ->
                    navigate(NormalPage.RecordVideo(date))
                },
                onRecapClick = {
                    navigate(NormalPage.RecapHub)
                },
                onDebugClick = {
                    navigate(NormalPage.Debug)
                },
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
                canGoBack = canGoBack,
                onBack = onBack,
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
                onExportRequested = { request ->
                    navigate(NormalPage.RecapExportProgress(request))
                },
            )

            is NormalPage.RecapExportProgress -> RecapExportProgressPage(
                viewModel = koinViewModel {
                    parametersOf(page.request)
                },
                share = share,
                onExit = {
                    pop()
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
