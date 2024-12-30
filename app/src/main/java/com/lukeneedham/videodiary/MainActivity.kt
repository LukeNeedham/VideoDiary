package com.lukeneedham.videodiary

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lukeneedham.videodiary.ui.Root
import com.lukeneedham.videodiary.ui.media.VideoPlayerPool

class MainActivity : ComponentActivity() {
    private val requiredPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Root()
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

    private fun requestRequiredPermissions() {
        ActivityCompat.requestPermissions(
            this,
            requiredPermissions.toTypedArray(),
            101
        )
    }

    private fun requestPermission(permission: String) {
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                101
            )
        }
    }
}
