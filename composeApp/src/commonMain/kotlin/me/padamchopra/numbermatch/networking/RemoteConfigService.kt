package me.padamchopra.numbermatch.networking

import me.padamchopra.numbermatch.remoteconfig.RemoteConfig

interface RemoteConfigService {
    fun <T> getValue(key: RemoteConfig<T>): T
}
