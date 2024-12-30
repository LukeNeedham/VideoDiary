package com.lukeneedham.videodiary.ui.navigation

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.checkvideo.CheckVideoPage
import com.lukeneedham.videodiary.ui.feature.exportdiary.ExportDiaryPage
import com.lukeneedham.videodiary.ui.feature.setup.SetupPage
import com.lukeneedham.videodiary.ui.feature.videorecorder.RecordVideoPage
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popAll
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

@Composable
fun Router(
    hasSetupCompleted: Boolean,
) {
    val startDestination = if (hasSetupCompleted) Page.Calendar else Page.Setup
    val navController = rememberNavController<Page>(startDestination = startDestination)

    fun navigate(to: Page) {
        Logger.debug("Navigating to: $to")
        navController.navigate(to)
    }

    fun pop() {
        val canPop = navController.backstack.entries.size > 1
        if (!canPop) return
        navController.pop()
    }

    NavBackHandler(navController)

    NavHost(
        controller = navController,
    ) { page ->
        when (page) {
            is Page.Setup -> SetupPage(
                viewModel = koinInject(),
                finishSetup = {
                    navController.popAll()
                    navigate(Page.Calendar)
                }
            )

            is Page.Calendar -> CalendarPage(
                viewModel = koinInject(),
                onRecordTodayVideoClick = {
                    navigate(Page.RecordVideo)
                },
                exportFullVideo = {
                    navigate(Page.ExportDiary)
                },
            )

            is Page.RecordVideo -> RecordVideoPage(
                viewModel = koinInject(),
                onRecordingFinished = { videoContentUri ->
                    navigate(
                        Page.CheckVideo(
                            date = LocalDate.now(),
                            videoContentUri = videoContentUri,
                        )
                    )
                }
            )

            is Page.CheckVideo -> CheckVideoPage(
                viewModel = koinInject {
                    parametersOf(page.date, page.videoContentUri)
                },
                onRetakeClick = {
                    pop()
                },
                onAccepted = {
                    navController.popUpTo { it is Page.Calendar }
                }
            )

            is Page.ExportDiary -> ExportDiaryPage(
                viewModel = koinInject(),
            )
        }
    }
}