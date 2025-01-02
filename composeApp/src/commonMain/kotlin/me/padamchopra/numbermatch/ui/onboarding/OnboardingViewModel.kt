package me.padamchopra.numbermatch.ui.onboarding

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.padamchopra.numbermatch.Async
import me.padamchopra.numbermatch.BaseViewModel
import me.padamchopra.numbermatch.navigation.NavAction
import me.padamchopra.numbermatch.navigation.Navigator
import me.padamchopra.numbermatch.navigation.Route
import me.padamchopra.numbermatch.repositories.AuthRepository
import me.padamchopra.numbermatch.repositories.OnboardingRepository
import me.padamchopra.numbermatch.utils.RemoteConfig
import me.padamchopra.numbermatch.utils.RemoteConfigProvider

class OnboardingViewModel(
    private val remoteConfigProvider: RemoteConfigProvider,
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository
): BaseViewModel<OnboardingScreen.State>(OnboardingScreen.State()) {

    init {
        setState {
            copy(
                usernameMinLength = remoteConfigProvider.resolve(RemoteConfig.UsernameMinLength),
            )
        }

        @OptIn(FlowPreview::class)
        viewModelScope.launch {
            stateFlow
                .map { state ->
                    state.username.takeIf { it.length >= state.usernameMinLength }
                }
                .filterNotNull()
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { username ->
                    suspend {
                        onboardingRepository.isUsernameUnique(username)
                    }.execute {
                        copy(
                            uniqueUsernameCheckJob = it.mapSuccess { data ->
                                OnboardingScreen.State.UsernameCheckResult(
                                    isUnique = data,
                                    username = username,
                                )
                            },
                            usernameUnique = false,
                        )
                    }
                }
        }

        onAsync(OnboardingScreen.State::uniqueUsernameCheckJob) { result ->
            setState {
                copy(
                    usernameUnique = result.isUnique && result.username == username,
                    showUsernameDuplicateError = !result.isUnique && result.username == username,
                )
            }
        }

        onAsync(OnboardingScreen.State::continueJob) {
            Navigator.execute(
                action = NavAction.ClearStackAndPush(Route.Home),
            )
        }
    }

    fun onBackClick() {
        withState { state ->
            if (state.backJob is Async.Loading) return@withState

            suspend {
                authRepository.signOut()
            }.execute {
                copy(
                    backJob = it,
                )
            }
        }
    }

    fun onNameValueChange(name: String): String {
        val filteredName = name.filter { it.isLetterOrDigit() || it.isWhitespace() }
        setState {
            copy(
                name = filteredName,
            )
        }
        return filteredName
    }

    fun onUsernameValueChange(username: String): String {
        val filteredUsername = username.filter { it.isLetterOrDigit() }
        setState {
            copy(
                username = filteredUsername,
                showUsernameDuplicateError = false,
            )
        }
        return filteredUsername
    }

    fun onContinueClick() {
        withState { state ->
            if (state.continueJob is Async.Loading) return@withState

            suspend {
                onboardingRepository.submitUserInfo(
                    name = state.name,
                    username = state.username,
                )
            }.execute {
                copy(
                    continueJob = it,
                )
            }
        }
    }
}
