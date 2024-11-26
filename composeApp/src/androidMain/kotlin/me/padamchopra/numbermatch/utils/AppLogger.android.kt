package me.padamchopra.numbermatch.utils

import android.util.Log

actual object AppLogger {
    actual fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }
}