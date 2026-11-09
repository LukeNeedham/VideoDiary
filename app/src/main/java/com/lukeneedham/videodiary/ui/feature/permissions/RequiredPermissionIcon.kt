package com.lukeneedham.videodiary.ui.feature.permissions

import android.Manifest
import androidx.annotation.DrawableRes
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission

internal object RequiredPermissionIcon {
    @DrawableRes
    fun of(permission: RequiredPermission): Int = when (permission.permission) {
        Manifest.permission.CAMERA -> R.drawable.camera
        Manifest.permission.RECORD_AUDIO -> R.drawable.mic
        else -> R.drawable.movie
    }
}
