package com.lukeneedham.videodiary.ui.navigation.setup

/**
 * The whole onboarding flow (intro slides + the setup wizard steps that follow) is presented as
 * one continuous sequence of pages, so a single page indicator can track progress across all of it.
 */
object SetupProgress {
    const val INTRO_SLIDE_COUNT = 4

    const val ORIENTATION_PAGE_INDEX = INTRO_SLIDE_COUNT
    const val RESOLUTION_PAGE_INDEX = INTRO_SLIDE_COUNT + 1
    const val DURATION_PAGE_INDEX = INTRO_SLIDE_COUNT + 2

    const val TOTAL_PAGE_COUNT = INTRO_SLIDE_COUNT + 3
}
