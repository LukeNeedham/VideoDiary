package com.lukeneedham.videodiary.domain.util.logger

import com.lukeneedham.videodiary.domain.util.logger.model.LoggerMessage

interface LoggerEngine {
    fun log(message: LoggerMessage)
}
