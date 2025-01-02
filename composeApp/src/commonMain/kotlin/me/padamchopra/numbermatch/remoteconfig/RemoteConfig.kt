package me.padamchopra.numbermatch.remoteconfig

interface RemoteConfig<T> {
    val key: String
    val defaultValue: T
}
