package com.lukeneedham.videodiary.domain.util.timer

import kotlin.time.Duration

sealed interface TimerTick {
    data class Ongoing(val duration: Duration) : TimerTick
    object Finished : TimerTick
}