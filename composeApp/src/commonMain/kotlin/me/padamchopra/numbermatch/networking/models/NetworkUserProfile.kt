package me.padamchopra.numbermatch.networking.models

import me.padamchopra.numbermatch.models.users.UserProfile

data class NetworkUserProfile(
    override val id: String,
    val username: String,
): NetworkEntity {
    companion object {
        const val USERNAME_KEY = "username"

        fun from(userProfile: UserProfile): NetworkUserProfile {
            return NetworkUserProfile(
                id = userProfile.user.id,
                username = userProfile.username,
            )
        }
    }

    override fun toMap(): Map<String, Any> {
        return mapOf(
            USERNAME_KEY to username,
        )
    }
}
