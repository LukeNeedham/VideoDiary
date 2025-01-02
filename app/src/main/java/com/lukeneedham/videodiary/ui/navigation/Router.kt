package com.lukeneedham.videodiary.ui.navigation

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.exportdiary.ExportDiaryPage
import com.lukeneedham.videodiary.ui.feature.record.check.CheckVideoPage
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoPage
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationPage
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationPage
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionPage
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popAll
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

@Composable
fun Router(
    needsSetup: Boolean,
    share: (ShareRequest) -> Unit,
    setOrientation: (Orientation) -> Unit,
) {
    val startDestination = if (needsSetup) Page.Setup.SelectOrientation else Page.Calendar
    val navController = rememberNavController<Page>(startDestination = startDestination)

    val canGoBack = navController.backstack.entries.size > 1

    fun navigate(to: Page) {
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
            Page.Setup.SelectOrientation -> SetupSelectOrientationPage(
                viewModel = koinViewModel(),
                onContinue = {
                    navigate(Page.Setup.SelectResolution)
                },
                setOrientation = setOrientation, canGoBack = canGoBack, onBack = onBack,
            )

            is Page.Setup.SelectResolution -> SetupSelectResolutionPage(
                viewModel = koinViewModel(),
                onContinue = {
                    navigate(Page.Setup.SelectVideoDuration)
                },
                canGoBack = canGoBack, onBack = onBack,
            )

            Page.Setup.SelectVideoDuration -> SelectVideoDurationPage(
                viewModel = koinViewModel(),
                onContinue = {
                    navController.popAll()
                    navigate(Page.Calendar)
                },
                canGoBack = canGoBack, onBack = onBack,
            )

            is Page.Calendar -> CalendarPage(
                viewModel = koinViewModel(),
                onRecordTodayVideoClick = {
                    navigate(Page.RecordVideo)
                },
                exportFullVideo = {
                    navigate(Page.ExportDiary)
                },
                share = share,
            )

            is Page.RecordVideo -> RecordVideoPage(
                viewModel = koinViewModel(),
                onRecordingFinished = { videoContentUri ->
                    navigate(
                        Page.CheckVideo(
                            date = LocalDate.now(),
                            videoContentUri = videoContentUri,
                        )
                    )
                },
                onBack = onBack,
            )

            is Page.CheckVideo -> {
                val returnToCalendar: () -> Unit = {
                    navController.popUpTo { it is Page.Calendar }
                }
                CheckVideoPage(
                    viewModel = koinViewModel {
                        parametersOf(page.date, page.videoContentUri)
                    },
                    onRetakeClick = {
                        pop()
                    },
                    onAccepted = returnToCalendar,
                    onCancelClick = returnToCalendar,
                )
            }

            is Page.ExportDiary -> ExportDiaryPage(
                viewModel = koinViewModel(),
                share = share,
                canGoBack = canGoBack,
                onBack = onBack,
            )
        }
    }
}