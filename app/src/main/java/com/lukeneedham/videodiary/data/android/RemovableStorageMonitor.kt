package com.lukeneedham.videodiary.data.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.lukeneedham.common.logger.Logger
import com.lukeneedham.videodiary.data.persistence.VideosDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Tells [VideosDao] when the SD card may have been inserted or removed, so the app can react
 * to it while it is running. Call [start] when the app comes to the foreground and [stop] when
 * it leaves, and [recheck] whenever it is resumed in case an event was missed.
 */
class RemovableStorageMonitor(
    private val context: Context,
    private val videosDao: VideosDao,
    ioDispatcher: CoroutineDispatcher,
) {
    private val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    private var isStarted = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Logger.d("Storage event: ${intent.action}")
            recheck()
        }
    }

    fun start() {
        if (!isStarted) {
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_MEDIA_MOUNTED)
                addAction(Intent.ACTION_MEDIA_UNMOUNTED)
                addAction(Intent.ACTION_MEDIA_EJECT)
                addAction(Intent.ACTION_MEDIA_REMOVED)
                addAction(Intent.ACTION_MEDIA_BAD_REMOVAL)
                addAction(Intent.ACTION_MEDIA_UNMOUNTABLE)
                // These events only carry a "file" URI, and are not delivered without this
                addDataScheme("file")
            }
            context.registerReceiver(receiver, filter)
            isStarted = true
        }
        recheck()
    }

    fun stop() {
        if (isStarted) {
            context.unregisterReceiver(receiver)
            isStarted = false
        }
    }

    fun recheck() {
        scope.launch {
            try {
                videosDao.initialiseStorageLocation()
            } catch (e: Exception) {
                Logger.w("Could not re-check the storage location", e)
            }
        }
    }
}
