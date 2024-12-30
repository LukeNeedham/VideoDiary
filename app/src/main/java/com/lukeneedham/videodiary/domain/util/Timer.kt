package com.lukeneedham.videodiary.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** A timer based on coroutines */
class Timer {
    private var runningTimerJob: Job? = null

    /**
     * Start the timer ticking once every [tickMillis].
     * [onTick] will be called on each tick (every time [tickMillis] passes).
     * The timer will be tied to [scope].
     */
    fun start(
        tickMillis: Long,
        endAtMillis: Long,
        scope: CoroutineScope,
        onTick: (tick: TimerTick) -> Unit
    ) {
        val endAtDuration = endAtMillis.milliseconds

        val tickDuration = tickMillis.milliseconds

        // First always kill any currently running timer
        stop()

        val timer: Flow<Duration> = (1..Long.MAX_VALUE)
            .asSequence()
            .asFlow()
            .onEach { delay(tickDuration) }
            .map { (it * tickMillis).milliseconds }

        runningTimerJob = scope.launch {
            timer.collect { elapsed ->
                if (elapsed > endAtDuration) {
                    onTick(TimerTick.Finished)
                    stop()
                } else {
                    onTick(TimerTick.Ongoing(elapsed))
                }
            }
        }
    }

    /** Kills the current timer, if one is running. */
    fun stop() {
        runningTimerJob?.cancel()
    }
}
