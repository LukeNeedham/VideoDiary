package com.lukeneedham.videodiary.ui.navigation.setup

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationPage
import com.lukeneedham.videodiary.ui.feature.setup.intro.SetupIntroPage
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationPage
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionPage
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupRouter(
    onSetupComplete: () -> Unit,
    setOrientation: (Orientation) -> Unit,
) {
    val navController = rememberNavController<SetupPage>(startDestination = SetupPage.Intro)

    val canGoBack = navController.backstack.entries.size > 1

    fun navigate(to: SetupPage) {
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
            SetupPage.Intro -> SetupIntroPage(
                onContinue = {
                    navigate(SetupPage.SelectOrientation)
                },
                canGoBack = canGoBack,
                onBack = onBack,
            )

            SetupPage.SelectOrientation -> SetupSelectOrientationPage(
                viewModel = koinViewModel(),
                onContinue = {
                    navigate(SetupPage.SelectResolution)
                },
                setOrientation = setOrientation,
                canGoBack = canGoBack,
                onBack = onBack,
            )

            is SetupPage.SelectResolution -> SetupSelectResolutionPage(
                viewModel = koinViewModel(),
                onContinue = {
                    navigate(SetupPage.SelectVideoDuration)
                },
                canGoBack = canGoBack,
                onBack = onBack,
            )

            SetupPage.SelectVideoDuration -> SelectVideoDurationPage(
                viewModel = koinViewModel(),
                onContinue = {
                    onSetupComplete()
                },
                canGoBack = canGoBack,
                onBack = onBack,
            )
        }
    }
}