package me.padamchopra.numbermatch.remoteconfig

interface LongRemoteConfig : RemoteConfig<Long>

object UsernameMinLengthRemoteConfig : LongRemoteConfig {
    override val key = "username_min_length"
    override val defaultValue = 5L
}
