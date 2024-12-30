package com.lukeneedham.videodiary.domain.util.logger

import com.lukeneedham.videodiary.domain.util.logger.model.LoggerMessage

/**
 * All logging, from all modules, should go through this class.
 * Do not use the Android Log directly, as that will break during unit testing.
 * This is a convenience singleton for logging,
 * so you don't have to inject the logger everywhere
 */
object Logger {
    private var engine: LoggerEngine? = null

    /** Holds the past messages */
    private val history = mutableListOf<LoggerMessage>()

    /** Make sure you set the logger as early as possible */
    fun setLoggerEngine(l: LoggerEngine) {
        engine = l
    }

    fun getHistory() = history.toList()

    fun log(message: LoggerMessage) {
        history.add(message)
        requireEngine().log(message)
    }

    /** Use debugs for logs required for testing purposes. */
    fun debug(message: String) {
        log(LoggerMessage.Debug(message))
    }

    /**
     * Use warnings for error states that are probably expected and well-handled,
     * but may possibly indicate a bug in the code.
     */
    fun warning(message: String, throwable: Throwable? = null) {
        log(LoggerMessage.Warning(message, throwable))
    }

    /** Use errors for error states that are unexpected, and probably indicate a bug in the code. */
    fun error(message: String, throwable: Throwable? = null) {
        log(LoggerMessage.Error(message, throwable))
    }

    private fun requireEngine(): LoggerEngine = engine ?: kotlin.error("Logger is not set!")
}
