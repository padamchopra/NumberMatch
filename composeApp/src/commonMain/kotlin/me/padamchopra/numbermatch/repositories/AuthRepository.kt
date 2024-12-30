package me.padamchopra.numbermatch.repositories

import kotlinx.coroutines.flow.MutableSharedFlow
import me.padamchopra.numbermatch.models.users.User
import me.padamchopra.numbermatch.networking.AuthDataSource

class AuthRepository(
    private val dataSource: AuthDataSource,
): Repository {
    private val _userSignedOutFlow: MutableSharedFlow<Unit> = MutableSharedFlow()
    val userSignedOutFlow = _userSignedOutFlow

    suspend fun signIn(email: String, password: String) {
        dataSource.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String) {
        dataSource.signUp(email, password)
    }

    suspend fun getCurrentUser(): User? {
        return dataSource.getCurrentUser()
    }

    suspend fun getCurrentUserId(): String? {
        return dataSource.getCurrentUser()?.id
    }

    suspend fun signOut() {
        dataSource.signOut()
        _userSignedOutFlow.emit(Unit)
    }

    suspend fun updateUser(user: User) {
        dataSource.updateUser(user)
    }

    suspend fun sendPasswordResetEmail(email: String) {
        dataSource.sendPasswordResetEmail(email = email)
    }
}
