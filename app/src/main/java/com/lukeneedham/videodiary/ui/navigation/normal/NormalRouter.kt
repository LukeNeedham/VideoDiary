package com.lukeneedham.videodiary.ui.navigation.normal

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPage
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.ExportDiaryCreatePage
import com.lukeneedham.videodiary.ui.feature.exportdiary.view.ExportDiaryViewPage
import com.lukeneedham.videodiary.ui.feature.record.check.CheckVideoPage
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoPage
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popUpTo
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

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
                onRecordTodayVideoClick = {
                    navigate(NormalPage.RecordVideo)
                },
                exportFullVideo = {
                    navigate(NormalPage.ExportDiaryCreate)
                },
                share = share,
            )

            is NormalPage.RecordVideo -> RecordVideoPage(
                viewModel = koinViewModel(),
                onRecordingFinished = { videoContentUri ->
                    navigate(
                        NormalPage.CheckVideo(
                            date = LocalDate.now(),
                            videoContentUri = videoContentUri,
                        )
                    )
                },
                onBack = onBack,
            )

            is NormalPage.CheckVideo -> {
                val returnToCalendar: () -> Unit = {
                    navController.popUpTo { it is NormalPage.Calendar }
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

            is NormalPage.ExportDiaryCreate -> ExportDiaryCreatePage(
                viewModel = koinViewModel(),
                canGoBack = canGoBack,
                onBack = onBack,
                onExported = {
                    navigate(NormalPage.ExportDiaryView(it))
                }
            )

            is NormalPage.ExportDiaryView -> ExportDiaryViewPage(
                viewModel = koinViewModel(),
                share = share,
                canGoBack = canGoBack,
                onBack = onBack,
                exportedVideo = page.exportedVideo,
            )
        }
    }
}