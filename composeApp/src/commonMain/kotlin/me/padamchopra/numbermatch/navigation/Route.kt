package me.padamchopra.numbermatch.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Auth: Route()

    @Serializable
    data object SplashScreen: Route()

    @Serializable
    data object Onboarding: Route()

    @Serializable
    data object Home: Route()
}
