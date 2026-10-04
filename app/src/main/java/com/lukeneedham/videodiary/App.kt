package com.lukeneedham.videodiary

import android.app.Application
import com.lukeneedham.videodiary.data.persistence.CrashLogDao
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.di.KoinModule
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.domain.util.logger.android.AndroidLoggerEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Set logger as soon as possible
        Logger.setLoggerEngine(AndroidLoggerEngine())

        startKoin {
            modules(KoinModule.modules)
            androidContext(this@App)
        }

        installCrashLogHandler()
        preloadVideoAspectRatio()
    }

    /** Warms the aspect ratio cache, so pages can read it synchronously on their first frame. */
    private fun preloadVideoAspectRatio() {
        val videoResolutionRepository = GlobalContext.get().get<VideoResolutionRepository>()
        CoroutineScope(Dispatchers.IO).launch {
            videoResolutionRepository.getAspectRatio()
        }
    }

    /** Persists fatal crashes to disk so they can be viewed later from the Debug page. */
    private fun installCrashLogHandler() {
        val crashLogDao = GlobalContext.get().get<CrashLogDao>()
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                crashLogDao.saveCrashLog(throwable)
            } catch (e: Exception) {
                Logger.error("Failed to save crash log", e)
            }
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
