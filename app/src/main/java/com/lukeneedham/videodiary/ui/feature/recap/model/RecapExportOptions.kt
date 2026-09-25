package com.lukeneedham.videodiary.ui.feature.recap.model

/**
 * Choices affecting how a recap gets stitched into a single video file for sharing. Exported
 * files are cached per [cacheKey], so each distinct combination of options gets its own file
 * rather than colliding with (or being mistaken for) a different one - when adding a new option,
 * fold it into [cacheKey] too.
 */
data class RecapExportOptions(
    val includeDateStamp: Boolean,
) {
    val cacheKey: String
        get() = "dateStamp-$includeDateStamp"
}
