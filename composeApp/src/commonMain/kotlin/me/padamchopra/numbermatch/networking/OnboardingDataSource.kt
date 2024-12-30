package me.padamchopra.numbermatch.networking

import me.padamchopra.numbermatch.models.users.UserProfile
import me.padamchopra.numbermatch.networking.models.FieldComparator
import me.padamchopra.numbermatch.networking.models.NetworkUserProfile
import me.padamchopra.paysy.networking.RemoteDataPath

class OnboardingDataSource(
    private val firestoreService: FirestoreService,
) {
    suspend fun hasUserFinishedOnboarding(currentUserId: String): Boolean {
        return firestoreService.doesDocumentExist(RemoteDataPath.Document.User(currentUserId))
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        val networkEntity = NetworkUserProfile.from(profile)
        firestoreService.setDocument(
            path = RemoteDataPath.Document.User(networkEntity.id),
            data = networkEntity.toMap(),
        )
    }

    suspend fun isUsernameUnique(username: String): Boolean {
        return firestoreService.comparisonMatchSize(
            collection = RemoteDataPath.Collection.Users,
            comparator = FieldComparator(
                name = NetworkUserProfile.USERNAME_KEY,
                value = username,
                operator = FieldComparator.Operator.Equal,
            ),
        ) == 0
    }
}
