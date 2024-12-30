package com.lukeneedham.videodiary

import android.content.Context

// dirty hack for static context
object AppContext {
    var value: Context? = null
}