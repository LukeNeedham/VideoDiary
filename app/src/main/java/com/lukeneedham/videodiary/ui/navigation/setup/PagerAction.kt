package com.lukeneedham.videodiary.ui.navigation.setup

/**
 * Describes the primary action for whichever page is currently shown in the onboarding pager, so
 * a single button can live outside the pager itself - static, rather than swiping along with the
 * page content.
 */
data class PagerAction(
    val label: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)
