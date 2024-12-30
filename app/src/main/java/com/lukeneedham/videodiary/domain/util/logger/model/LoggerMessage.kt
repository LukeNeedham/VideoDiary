package com.lukeneedham.videodiary.domain.util.logger.model

sealed interface LoggerMessage {
    data class Debug(val message: String) : LoggerMessage
    data class Warning(val message: String, val throwable: Throwable?) : LoggerMessage
    data class Error(val message: String, val throwable: Throwable?) : LoggerMessage
}
