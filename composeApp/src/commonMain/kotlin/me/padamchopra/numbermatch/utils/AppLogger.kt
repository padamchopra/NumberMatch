package me.padamchopra.numbermatch.utils

expect object AppLogger {
    fun debug(tag: String, message: String)

    fun error(tag: String, message: String)
}
