package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.domain.model.CrashLog
import com.lukeneedham.videodiary.domain.util.logger.Logger
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant

class CrashLogDao(
    private val context: Context,
) {
    private val crashLogsDir = File(context.filesDir, "crashlogs").apply {
        mkdirs()
    }

    /** Persists a fatal crash to disk. Called from the uncaught exception handler. */
    fun saveCrashLog(throwable: Throwable) {
        val timestamp = Instant.now()
        val stackTrace = StringWriter().also { throwable.printStackTrace(PrintWriter(it)) }.toString()
        File(crashLogsDir, "${timestamp.toEpochMilli()}.txt").writeText(stackTrace)
    }

    fun getAllCrashLogs(): List<CrashLog> {
        val files = crashLogsDir.listFiles() ?: return emptyList()
        return files.mapNotNull { file ->
            val timestampMillis = file.nameWithoutExtension.toLongOrNull()
            if (timestampMillis == null) {
                Logger.warning("Skipping crash log with unrecognised file name: ${file.name}")
                return@mapNotNull null
            }
            CrashLog(
                timestamp = Instant.ofEpochMilli(timestampMillis),
                stackTrace = file.readText(),
            )
        }.sortedByDescending { it.timestamp }
    }
}
