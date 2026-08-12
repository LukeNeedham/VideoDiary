package com.lukeneedham.videodiary.ui.navigation.setup

import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

/**
 * The whole onboarding flow (intro slides, permission requests, and the setup wizard steps that
 * follow) is presented as one continuous sequence of pages, so a single page indicator can track
 * progress across all of it.
 */
object SetupProgress {
    const val INTRO_SLIDE_COUNT = 4

    val PERMISSION_COUNT = RequiredPermissions.permissions.size
    val PERMISSIONS_START_INDEX = INTRO_SLIDE_COUNT

    val ORIENTATION_PAGE_INDEX = INTRO_SLIDE_COUNT + PERMISSION_COUNT
    val RESOLUTION_PAGE_INDEX = ORIENTATION_PAGE_INDEX + 1
    val DURATION_PAGE_INDEX = ORIENTATION_PAGE_INDEX + 2

    val TOTAL_PAGE_COUNT = ORIENTATION_PAGE_INDEX + 3
}
