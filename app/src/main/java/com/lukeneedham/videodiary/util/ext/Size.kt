package com.lukeneedham.videodiary.util.ext

import android.util.Size

fun Size.aspectRatio() = width / height.toFloat()