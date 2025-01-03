package com.lukeneedham.videodiary.ui.fileprovider

import com.lukeneedham.videodiary.BuildConfig

object FileProviderName {
    /** Must match the value defined for `FileProvider` the Manifest */
    const val authority = BuildConfig.APPLICATION_ID + ".provider"
}
