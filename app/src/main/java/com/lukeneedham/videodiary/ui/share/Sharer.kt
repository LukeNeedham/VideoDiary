package com.lukeneedham.videodiary.ui.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.fileprovider.FileProviderName


class Sharer(
    private val context: Context,
) {
    fun createShareIntent(request: ShareRequest): Intent {
        val videoUri = FileProvider.getUriForFile(
            context,
            FileProviderName.authority,
            request.video,
        )

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "video/mp4"
            putExtra(Intent.EXTRA_TEXT, request.text)
            putExtra(Intent.EXTRA_STREAM, videoUri)
        }
        return Intent.createChooser(intent, null)
    }
}
