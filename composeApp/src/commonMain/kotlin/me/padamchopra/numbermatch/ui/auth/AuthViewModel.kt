package me.padamchopra.paysy.ui.auth

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.padamchopra.numbermatch.Async
import me.padamchopra.numbermatch.BaseViewModel
import me.padamchopra.numbermatch.models.StringData
import me.padamchopra.numbermatch.ui.auth.AuthScreen
import me.padamchopra.numbermatch.utils.ShowSnackBar
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.auth_email_empty

class AuthViewModel: BaseViewModel<AuthScreen.State>(initialState = AuthScreen.State()) {
    init {
//        onAsync(AuthScreen.State::continueJob) { authType ->
//            when (authType) {
//                AuthScreen.State.AuthType.SignIn -> {
//                    suspend {
//                        onboardingRepository.hasUserFinishedOnboarding()
//                    }.execute { job ->
//                        copy(
//                            hasFinishedOnboardingJob = job,
//                        )
//                    }
//                }
//                AuthScreen.State.AuthType.SignUp -> {
//                    viewModelScope.launch {
//                        Navigator.execute(
//                            NavAction.ClearStackAndPush(route = Route.Onboarding),
//                        )
//                    }
//                }
//            }
//        }

//        onAsync(
//            AuthScreen.State::hasFinishedOnboardingJob,
//        ) { hasFinishedOnboarding ->
//            val route = when (hasFinishedOnboarding) {
//                true -> Route.Home
//                false -> Route.Onboarding
//            }
//            viewModelScope.launch {
//                Navigator.execute(
//                    NavAction.ClearStackAndPush(route = route),
//                )
//            }
//        }
//
//        onAsync(AuthScreen.State::forgotPasswordJob) {
//            ShowSnackBar.success(StringData.Resource(Res.string.auth_forgot_password_email_sent))
//        }
    }

    fun onSwitchAuthTypeClick() {
        setState {
            if (continueJob is Async.Loading || continueJob is Async.Success) {
                return@setState this
            }
            copy(
                authType = when (authType) {
                    AuthScreen.State.AuthType.SignIn -> AuthScreen.State.AuthType.SignUp
                    AuthScreen.State.AuthType.SignUp -> AuthScreen.State.AuthType.SignIn
                },
            )
        }
    }

    fun onForgotPasswordClick() {
        withState { state ->
            if (state.forgotPasswordJob is Async.Loading) return@withState

            if (state.email.isBlank()) {
                viewModelScope.launch {
                    ShowSnackBar.error(StringData.Resource(Res.string.auth_email_empty))
                }
                return@withState
            }

//            suspend {
//                authRepository.sendPasswordResetEmail(email = state.email)
//            }.execute {
//                copy(
//                    forgotPasswordJob = it,
//                )
//            }
        }
    }

    fun onEmailChange(email: String): String {
        val trimmedEmail = email.trim()
        setState {
            copy(email = trimmedEmail)
        }
        return trimmedEmail
    }

    fun onPasswordChange(password: String): String {
        setState {
            copy(password = password)
        }
        return password
    }

    fun onContinueButtonClick() {
        withState { state ->
            if (state.continueJob is Async.Loading) return@withState

            suspend {
//                when (state.authType) {
//                    AuthScreen.State.AuthType.SignIn -> {
//                        authRepository.signIn(
//                            email = state.email,
//                            password = state.password,
//                        )
//                    }
//                    AuthScreen.State.AuthType.SignUp -> {
//                        authRepository.signUp(
//                            email = state.email,
//                            password = state.password,
//                        )
//                    }
//                }
                state.authType
            }.execute {
                copy(
                    continueJob = it,
                )
            }
        }
    }
}
