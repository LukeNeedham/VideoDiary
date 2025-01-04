package com.lukeneedham.videodiary.ui.permissions

import android.Manifest
import android.os.Build

object RequiredPermissions {
    /** Permission for file access to MediaStore is required only on older Android versions */
    private val fileAccessPermission = if (Build.VERSION.SDK_INT <= 28) {
        /**
         * https://developer.android.com/training/data-storage/shared/media#extra-permissions
         * If your app is used on a device that runs Android 9 or lower,
         * or if your app has temporarily opted out of scoped storage,
         * you must request the READ_EXTERNAL_STORAGE permission to access any media file.
         * If you want to modify media files,
         * you must request the WRITE_EXTERNAL_STORAGE permission, as well.
         */
        RequiredPermission(
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            displayName = "File access",
            description = "The app needs permission to write files so your videos can be saved on your device",
        )
    } else {
        /**
         * https://developer.android.com/training/data-storage/shared/media#storage-permission-not-always-needed
         * On devices that run Android 10 or higher,
         * you don't need storage-related permissions to access and modify media files that your app owns,
         * including files in the MediaStore.Downloads collection.
         * If you're developing a camera app, for example,
         * you don't need to request storage-related permissions to access the photos it takes,
         * because your app owns the images that you're writing to the media store.
         */
        null
    }

    val permissions = listOfNotNull(
        RequiredPermission(
            permission = Manifest.permission.CAMERA,
            displayName = "Camera",
            description = "The app needs camera access so it can record your videos",
        ),
        RequiredPermission(
            permission = Manifest.permission.RECORD_AUDIO,
            displayName = "Record Audio",
            description = "The app needs permission to record audio so your videos have sound in them",
        ),
        fileAccessPermission,
    )
}