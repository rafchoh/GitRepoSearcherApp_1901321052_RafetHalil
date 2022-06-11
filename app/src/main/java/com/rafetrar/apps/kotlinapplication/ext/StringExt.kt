package com.rafetrar.apps.kotlinapplication.ext

import android.util.Base64
import java.nio.charset.Charset

fun String.decode(): String {
    val data: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return String(data, Charset.forName("UTF-8"))
}