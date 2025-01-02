package com.lukeneedham.videodiary.ui.permissions

import android.Manifest

object RequiredPermissions {
    val permissions = listOf(
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
        RequiredPermission(
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            displayName = "File access",
            description = "The app needs permission to write files so your videos can be saved on your device",
        ),
    )
}