package com.lukeneedham.videodiary.domain.util.logger

import com.lukeneedham.videodiary.domain.util.logger.android.AndroidLoggerEngine
import com.lukeneedham.videodiary.domain.util.logger.mock.NoopLoggerEngine

object LoggerOrigin {
    /** The names of classes that are related to logging. Make sure to register them here. */
    private val loggingClassNames = listOf(
        this::class,
        LoggerOrigin::class,
        AndroidLoggerEngine::class,
        NoopLoggerEngine::class,
        LoggerEngine::class,
        Logger::class,
    ).map { it.qualifiedName }

    fun getOrigin(): String {
        val stackTrace = StackTraceCatcher().stackTrace

        /**
         * The call-site that we're interested in is the original log requester,
         * which is the first element in the stack trace that isn't logger related.
         */
        val callSite = stackTrace.first { it.className !in loggingClassNames }
        val fileName = callSite.fileName
        val line = callSite.lineNumber

        /** This is a special string format that Logcat will interpret as a hyperlink */
        val lineLink = "($fileName:$line)"
        return lineLink
    }

    /**
     * This is a little trick to get the stack trace.
     * It works because when a [Throwable] is created,
     * the current stack trace is caught.
     */
    private class StackTraceCatcher : Throwable()
}
