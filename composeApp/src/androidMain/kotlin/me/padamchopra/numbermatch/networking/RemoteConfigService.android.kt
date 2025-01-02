package me.padamchopra.numbermatch.networking

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import me.padamchopra.numbermatch.Constants
import me.padamchopra.numbermatch.remoteconfig.LongRemoteConfig
import me.padamchopra.numbermatch.remoteconfig.RemoteConfig

class RemoteConfigServiceImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
): RemoteConfigService {
    init {
        firebaseRemoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = Constants.REMOTE_CONFIG_FETCH_INTERVAL
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getValue(key: RemoteConfig<T>): T {
        val value = firebaseRemoteConfig.getValue(key.key)
        return when (key) {
            is LongRemoteConfig -> value.asLong()
            else -> throw IllegalStateException("Unsupported RemoteConfig type")
        } as T
    }
}
