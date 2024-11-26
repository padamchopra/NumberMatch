package me.padamchopra.numbermatch.navigation

sealed class NavAction {
    data class GoTo<T: Route>(val route: T): NavAction()

    data class ClearStackAndPush<T : Route>(val route: T): NavAction()

    data object Pop: NavAction()
}
