package com.lukeneedham.videodiary

import android.app.Application
import com.lukeneedham.videodiary.di.KoinModule
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.domain.util.logger.android.AndroidLoggerEngine
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.mp4parser.ParserPropertiesProvider
import org.mp4parser.ParserPropertiesProviderHolder

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Set logger as soon as possible
        Logger.setLoggerEngine(AndroidLoggerEngine())

        // Setup mp4parser to load the properties file from assets (so it works on Android)
        ParserPropertiesProviderHolder.get = ParserPropertiesProvider {
            assets.open("isoparser2-default.properties")
        }

        startKoin {
            modules(KoinModule.modules)
            androidContext(this@App)
        }
    }
}
