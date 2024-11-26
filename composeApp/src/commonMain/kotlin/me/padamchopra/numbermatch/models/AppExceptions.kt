package me.padamchopra.numbermatch.models

import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.auth_email_exception
import numbermatch.composeapp.generated.resources.auth_invalid_credentials_exception
import numbermatch.composeapp.generated.resources.auth_user_collision_exception
import numbermatch.composeapp.generated.resources.auth_weak_password_exception
import org.jetbrains.compose.resources.StringResource

sealed class AppExceptions(open val messageResource: StringResource, override val cause: Throwable?): Exception(cause) {
    sealed class Auth(
        override val messageResource: StringResource,
        override val cause: Throwable?,
    ): AppExceptions(messageResource, cause) {
        class EmailException(override val cause: Throwable?): Auth(Res.string.auth_email_exception, cause)
        class WeakPasswordException(override val cause: Throwable?): Auth(Res.string.auth_weak_password_exception, cause)
        class InvalidCredentialsException(override val cause: Throwable?): Auth(Res.string.auth_invalid_credentials_exception, cause)
        class UserCollisionException(override val cause: Throwable?): Auth(Res.string.auth_user_collision_exception, cause)
    }
}
