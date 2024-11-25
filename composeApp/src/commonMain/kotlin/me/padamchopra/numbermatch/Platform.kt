package me.padamchopra.numbermatch

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform