package com.lukeneedham.videodiary.domain.model

/** Where diary video files are kept. */
sealed interface VideoStorageLocation {
    data object Internal : VideoStorageLocation
    data object RemovableStorage : VideoStorageLocation
}
