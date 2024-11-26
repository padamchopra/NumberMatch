package me.padamchopra.numbermatch.utils

class RemoteConfigProvider {
    fun <T> resolve(remoteConfig: RemoteConfig<T>): T {
        return remoteConfig.defaultValue
    }
}
