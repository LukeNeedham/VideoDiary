package com.lukeneedham.videodiary.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

class CurrentDateRepository(
    private val ioDispatcher: CoroutineDispatcher,
) {
    val currentDate: Flow<LocalDate> = flow {
        while (true) {
            val now = LocalDate.now()
            emit(now)
            val tomorrow = now.plusDays(1).atStartOfDay()
            val delayMillis = Duration.between(
                LocalDateTime.now(),
                tomorrow
            ).toMillis().coerceAtLeast(1000)
            delay(delayMillis)
        }
    }.distinctUntilChanged().flowOn(ioDispatcher)
}
