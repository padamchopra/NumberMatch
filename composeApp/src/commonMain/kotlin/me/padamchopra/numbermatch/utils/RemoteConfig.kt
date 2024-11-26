package me.padamchopra.numbermatch.utils

sealed class RemoteConfig<T>(
    val key: String,
    val defaultValue: T,
) {
    data object UsernameMinLength : RemoteConfig<Int>(
        key = "username_min_length",
        defaultValue = 5,
    )
}
