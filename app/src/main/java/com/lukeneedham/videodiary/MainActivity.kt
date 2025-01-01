package com.lukeneedham.videodiary

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.root.Root
import com.lukeneedham.videodiary.ui.media.VideoPlayerPool
import com.lukeneedham.videodiary.ui.share.Sharer
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val requiredPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private val sharer: Sharer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Root(
                share = ::share,
                setOrientation = ::setOrientation,
            )
        }

        requestRequiredPermissions()
    }

    override fun onPause() {
        super.onPause()
        VideoPlayerPool.onAppPause()
    }

    override fun onResume() {
        super.onResume()
        VideoPlayerPool.onAppResume()
    }

    private fun setOrientation(orientation: Orientation) {
        val info = when (orientation) {
            Orientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Orientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        requestedOrientation = info
    }

    private fun share(request: ShareRequest) {
        val intent = sharer.createShareIntent(request)
        startActivity(intent)
    }

    private fun requestRequiredPermissions() {
        ActivityCompat.requestPermissions(
            this,
            requiredPermissions.toTypedArray(),
            101
        )
    }
}
