package com.lukeneedham.videodiary

import android.app.Application
import com.lukeneedham.common.DebugKit
import com.lukeneedham.common.DebugKitConfig
import com.lukeneedham.common.logger.LogLevel
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.di.KoinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Set up the logger and crash handler as soon as possible, before anything can log or crash.
        // Fatal crashes are saved to disk and shown on the Debug > Crashes page.
        DebugKit.init(
            DebugKitConfig(
                enabled = BuildConfig.DEBUG,
                // Debug logs are only kept in debug builds
                minLogLevel = if (BuildConfig.DEBUG) LogLevel.Debug else LogLevel.Info,
            ),
        )

        startKoin {
            modules(KoinModule.modules)
            androidContext(this@App)
        }

        preloadVideoAspectRatio()
    }

    /** Warms the aspect ratio cache, so pages can read it synchronously on their first frame. */
    private fun preloadVideoAspectRatio() {
        val videoResolutionRepository = GlobalContext.get().get<VideoResolutionRepository>()
        CoroutineScope(Dispatchers.IO).launch {
            videoResolutionRepository.getAspectRatio()
        }
    }
}
