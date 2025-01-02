package me.padamchopra.numbermatch.remoteconfig

import me.padamchopra.numbermatch.networking.RemoteConfigService
import me.padamchopra.numbermatch.utils.AppLogger

class RemoteConfigProvider(
    private val remoteConfigService: RemoteConfigService,
) {
    fun <T> resolve(remoteConfig: RemoteConfig<T>): T {
        return try {
            remoteConfigService.getValue(remoteConfig)
        } catch (e: Exception) {
            AppLogger.error("RemoteConfigProvider", "Failed to fetch remote config ${remoteConfig.key}")
            remoteConfig.defaultValue
        }
    }
}
