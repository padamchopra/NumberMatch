package me.padamchopra.numbermatch.networking

import me.padamchopra.numbermatch.models.users.User

class AuthDataSource(
    private val authService: AuthService,
) {
    suspend fun signIn(email: String, password: String): User {
        return authService.signIn(email, password).unwrap()
    }

    suspend fun signUp(email: String, password: String): User {
        return authService.signUp(email, password).unwrap()
    }

    suspend fun getCurrentUser(): User? {
        return authService.getCurrentUser().unwrap()
    }

    suspend fun signOut() {
        authService.signOut()
    }

    suspend fun updateUser(user: User) {
        authService.updateUser(user)
    }

    suspend fun sendPasswordResetEmail(email: String) {
        authService.sendPasswordResetEmail(email).unwrap()
    }
}
