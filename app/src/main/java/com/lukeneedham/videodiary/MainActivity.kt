package com.lukeneedham.videodiary

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.media.VideoPlayerPool
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import com.lukeneedham.videodiary.ui.root.Root
import com.lukeneedham.videodiary.ui.share.Sharer
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val sharer: Sharer by inject()

    private val permissionResultListenerHolder = PermissionResultListenerHolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Root(
                share = ::share,
                setOrientation = ::setOrientation,
                requestPermission = ::requestPermission,
                permissionResultListenerHolder = permissionResultListenerHolder,
            )
        }
    }

    override fun onPause() {
        super.onPause()
        VideoPlayerPool.onAppPause()
    }

    override fun onResume() {
        super.onResume()
        VideoPlayerPool.onAppResume()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionResultListenerHolder.onPermissionResult()
    }

    private fun setOrientation(orientation: Orientation) {
        val info = when (orientation) {
            Orientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            Orientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
        requestedOrientation = info
    }

    private fun share(request: ShareRequest) {
        val intent = sharer.createShareIntent(request)
        startActivity(intent)
    }

    private fun requestPermission(
        permission: String,
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission),
            101,
        )
    }
}
