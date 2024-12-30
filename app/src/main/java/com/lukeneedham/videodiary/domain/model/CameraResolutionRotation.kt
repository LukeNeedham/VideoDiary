package com.lukeneedham.videodiary.domain.model

import android.util.Size

enum class CameraResolutionRotation(val degrees: Int) {
    R0(0),
    R90(90),
    R180(180),
    R270(270);

    fun rotate(resolution: Size) = when (this) {
        R0,
        R180 -> resolution

        // Aspect ratio corrected for rotation
        R90,
        R270 -> Size(resolution.height, resolution.width)
    }

    companion object {
        fun fromDegrees(degrees: Int) = entries.firstOrNull { it.degrees == degrees }
    }
}