package me.padamchopra.numbermatch.utils

import java.util.logging.Level
import java.util.logging.Logger

actual object AppLogger {
    private val logger = Logger.getAnonymousLogger()

    actual fun debug(tag: String, message: String) {
        logger.log(Level.FINE, "DEBUG $tag: $message")
    }
}