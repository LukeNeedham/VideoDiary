package com.lukeneedham.videodiary.domain.util.logger.android

import android.util.Log
import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.domain.util.logger.LoggerEngine
import com.lukeneedham.videodiary.domain.util.logger.LoggerOrigin
import com.lukeneedham.videodiary.domain.util.logger.model.LoggerMessage

class AndroidLoggerEngine : LoggerEngine {
    private val tag = "VideoDiaryLog"

    override fun log(message: LoggerMessage) {
        when (message) {
            is LoggerMessage.Debug -> {
                if (BuildConfig.DEBUG) {
                    Log.d(tag, withOrigin(message.message))
                }
            }

            is LoggerMessage.Error -> {
                Log.e(tag, withOrigin(message.message), message.throwable)
            }

            is LoggerMessage.Warning -> {
                Log.w(tag, withOrigin(message.message), message.throwable)
            }
        }
    }

    private fun withOrigin(message: String): String {
        val origin = LoggerOrigin.getOrigin()
        return "$origin: $message"
    }
}
