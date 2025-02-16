package com.lukeneedham.videodiary.util.ext

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.TextureOverlay
import com.google.common.collect.ImmutableList

@OptIn(UnstableApi::class)
fun TextureOverlay.toOverlayEffect(): OverlayEffect {
    val overlaysBuilder = ImmutableList.Builder<TextureOverlay>()
    overlaysBuilder.add(this)
    return OverlayEffect(overlaysBuilder.build())
}