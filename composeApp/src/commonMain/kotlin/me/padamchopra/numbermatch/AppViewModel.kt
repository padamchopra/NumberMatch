package me.padamchopra.numbermatch

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.padamchopra.numbermatch.models.SnackBarData
import me.padamchopra.numbermatch.navigation.NavAction
import me.padamchopra.numbermatch.navigation.Navigator
import me.padamchopra.numbermatch.navigation.Route
import me.padamchopra.numbermatch.repositories.AuthRepository
import me.padamchopra.numbermatch.repositories.OnboardingRepository
import me.padamchopra.numbermatch.utils.ShowSnackBar

class AppViewModel(
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
): BaseViewModel<AppViewModel.State>(State()) {
    data class State(
        val navAction: NavAction? = null,
        val snackBarData: SnackBarData? = null,
        val redirectRouteJob: Async<Route?> = Async.Uninitialized,
        val keepSplashScreenVisible: Boolean = true,
    ): UiState

    init {
        onAsync(
            State::redirectRouteJob,
            onFail = {
                setState {
                    copy(keepSplashScreenVisible = false)
                }
                when (getPlatform()) {
                    Platform.Android -> {
                        // no-op
                    }
                    Platform.IOS,
                    Platform.Desktop -> {
                        Navigator.execute(
                            NavAction.ClearStackAndPush(route = Route.Auth),
                        )
                    }
                }
            }
        ) { route ->
            if (route != null) {
                Navigator.execute(
                    NavAction.ClearStackAndPush(route = route),
                )
                delay(400)
            }
            setState {
                copy(keepSplashScreenVisible = false)
            }
        }

        viewModelScope.launch {
            Navigator.actionFlow.collectLatest { action ->
                setState {
                    copy(
                        navAction = action,
                    )
                }
            }
        }

        viewModelScope.launch {
            ShowSnackBar.dataFlow.collectLatest { data ->
                setState {
                    copy(
                        snackBarData = data,
                    )
                }
            }
        }

        viewModelScope.launch {
            authRepository.userSignedOutFlow.collectLatest {
                Navigator.execute(
                    action = NavAction.ClearStackAndPush(route = Route.Auth)
                )
            }
        }

        suspend {
            val user = authRepository.getCurrentUser()
            if (user == null) {
                when (getPlatform()) {
                    Platform.Android -> null
                    Platform.IOS -> Route.Auth
                    Platform.Desktop -> Route.Auth
                }
            } else {
                when (onboardingRepository.hasUserFinishedOnboarding()) {
                    true -> Route.Home
                    false -> Route.Onboarding
                }
            }
        }.execute {
            copy(
                redirectRouteJob = it,
            )
        }
    }

    fun onNavActionHandled() {
        setState {
            copy(
                navAction = null,
            )
        }
    }

    fun onSnackBarDataHandled() {
        setState {
            copy(
                snackBarData = null,
            )
        }
    }
}
