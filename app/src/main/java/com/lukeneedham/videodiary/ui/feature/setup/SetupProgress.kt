package com.lukeneedham.videodiary.ui.feature.setup

import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions

/**
 * The whole onboarding flow (intro slides, permission requests, and the setup wizard steps that
 * follow) is presented as one continuous sequence of pages, so a single page indicator can track
 * progress across all of it.
 *
 * The storage location page is only part of the sequence when [hasStoragePage]: it is only useful
 * when there is an SD card to choose.
 */
class SetupProgress(hasStoragePage: Boolean) {
    val PERMISSION_COUNT = RequiredPermissions.permissions.size
    val PERMISSIONS_START_INDEX = INTRO_SLIDE_COUNT

    val ORIENTATION_PAGE_INDEX = INTRO_SLIDE_COUNT + PERMISSION_COUNT

    /** Right after the orientation page, or -1 when there is no storage page */
    val STORAGE_PAGE_INDEX = if (hasStoragePage) ORIENTATION_PAGE_INDEX + 1 else -1

    private val afterStorageIndex = ORIENTATION_PAGE_INDEX + if (hasStoragePage) 2 else 1

    val RESOLUTION_PAGE_INDEX = afterStorageIndex
    val DURATION_PAGE_INDEX = afterStorageIndex + 1
    val FINAL_PAGE_INDEX = afterStorageIndex + 2

    val TOTAL_PAGE_COUNT = afterStorageIndex + 3

    companion object {
        const val INTRO_SLIDE_COUNT = 4
    }
}
