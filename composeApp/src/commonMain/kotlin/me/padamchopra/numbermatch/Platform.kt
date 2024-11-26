package me.padamchopra.numbermatch

enum class Platform {
    Android,
    IOS,
    Desktop
}

expect fun getPlatform(): Platform
