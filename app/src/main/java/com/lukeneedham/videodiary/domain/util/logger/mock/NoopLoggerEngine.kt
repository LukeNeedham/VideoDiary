package com.lukeneedham.videodiary.domain.util.logger.mock

import com.lukeneedham.videodiary.domain.util.logger.LoggerEngine
import com.lukeneedham.videodiary.domain.util.logger.model.LoggerMessage

/** Does no logging */
class NoopLoggerEngine : LoggerEngine {
    override fun log(message: LoggerMessage) {
        // No-op
    }
}
