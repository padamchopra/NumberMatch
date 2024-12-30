package me.padamchopra.numbermatch.networking

import me.padamchopra.numbermatch.models.DataOrException
import me.padamchopra.numbermatch.models.users.User

interface AuthService {
    suspend fun signIn(email: String, password: String): DataOrException<User>
    suspend fun signUp(email: String, password: String): DataOrException<User>
    suspend fun getCurrentUser(): DataOrException<User?>
    suspend fun signOut()
    suspend fun updateUser(user: User)
    suspend fun sendPasswordResetEmail(email: String): DataOrException<Boolean>
}
