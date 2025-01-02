package me.padamchopra.numbermatch.utils

import platform.Foundation.NSLog

actual object AppLogger {
    actual fun debug(tag: String, message: String) {
        NSLog("[DEBUG] [$tag] $message")
    }

    actual fun error(tag: String, message: String) {
        NSLog("[ERROR] [$tag] $message")
    }
}