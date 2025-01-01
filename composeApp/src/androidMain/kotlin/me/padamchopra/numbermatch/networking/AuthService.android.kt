package me.padamchopra.numbermatch.networking

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import me.padamchopra.numbermatch.models.AppExceptions
import me.padamchopra.numbermatch.models.DataOrException
import me.padamchopra.numbermatch.models.users.User

class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth,
): AuthService {
    override suspend fun signIn(email: String, password: String): DataOrException<User> {
        val result = try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw identifyException(e)
        }
        result.user?.let {
            return DataOrException.Data(
                User(
                    id = it.uid,
                    name = it.displayName,
                    avatarUrl = it.photoUrl?.toString(),
                )
            )
        } ?: throw IllegalStateException("User not found")
    }

    override suspend fun signUp(email: String, password: String): DataOrException<User> {
        val result = try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw identifyException(e)
        }
        result.user?.let {
            return DataOrException.Data(
                User(
                    id = it.uid,
                    name = it.displayName,
                    avatarUrl = it.photoUrl?.toString(),
                )
            )
        } ?: throw IllegalStateException("User not found")
    }

    override suspend fun getCurrentUser(): DataOrException<User?> {
        return DataOrException.Data(
            firebaseAuth.currentUser?.let {
                User(
                    id = it.uid,
                    name = it.displayName,
                    avatarUrl = it.photoUrl?.toString(),
                )
            }
        )
    }

    private fun identifyException(e: Exception): Exception {
        return when (e) {
            is FirebaseAuthEmailException -> {
                AppExceptions.Auth.EmailException(e.cause)
            }
            is FirebaseAuthWeakPasswordException -> {
                AppExceptions.Auth.WeakPasswordException(e.cause)
            }
            is FirebaseAuthInvalidCredentialsException -> {
                AppExceptions.Auth.InvalidCredentialsException(e.cause)
            }
            is FirebaseAuthUserCollisionException -> {
                AppExceptions.Auth.UserCollisionException(e.cause)
            }
            else -> e
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun updateUser(user: User) {
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(user.name)
                .setPhotoUri(user.avatarUrl?.toUri())
                .build()
        )?.await() ?: throw IllegalStateException("User not found")
    }

    override suspend fun sendPasswordResetEmail(email: String): DataOrException<Boolean> {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            return DataOrException.Data(true)
        } catch (e: Exception) {
            throw identifyException(e)
        }
    }
}