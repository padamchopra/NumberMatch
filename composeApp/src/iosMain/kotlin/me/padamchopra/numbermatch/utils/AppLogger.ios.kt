package me.padamchopra.numbermatch.utils

import platform.Foundation.NSLog

actual object AppLogger {
    actual fun debug(tag: String, message: String) {
        NSLog("[$tag] $message")
    }
}