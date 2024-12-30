package me.padamchopra.numbermatch.repositories

import me.padamchopra.numbermatch.models.users.UserProfile
import me.padamchopra.numbermatch.networking.OnboardingDataSource

class OnboardingRepository(
    private val authRepository: AuthRepository,
    private val dataSource: OnboardingDataSource,
): Repository {
    suspend fun hasUserFinishedOnboarding(): Boolean {
        val currentUserId =
            authRepository.getCurrentUserId() ?: throw IllegalStateException("User not signed in")
        return dataSource.hasUserFinishedOnboarding(
            currentUserId = currentUserId,
        )
    }

    suspend fun submitUserInfo(
        name: String,
        username: String,
    ) {
        val currentUser = authRepository.getCurrentUser() ?: throw IllegalStateException("User not signed in")
        val profile = UserProfile(
            user = currentUser.copy(
                name = name,
            ),
            username = username,
        )
        if (currentUser.name != name) {
            authRepository.updateUser(user = profile.user)
        }
        dataSource.updateUserProfile(profile = profile)
    }

    suspend fun isUsernameUnique(username: String): Boolean {
        return dataSource.isUsernameUnique(username = username)
    }
}
