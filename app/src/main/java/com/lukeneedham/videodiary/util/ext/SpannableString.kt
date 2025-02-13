package com.lukeneedham.videodiary.util.ext

import android.text.Spannable
import android.text.SpannableString

fun SpannableString.setFullLengthSpan(what: Any) {
    setSpan(what, 0, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
}