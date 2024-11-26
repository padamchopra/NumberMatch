package me.padamchopra.numbermatch.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.padamchopra.numbermatch.Platform
import me.padamchopra.numbermatch.getPlatform
import me.padamchopra.numbermatch.ui.auth.AuthScreen
import me.padamchopra.numbermatch.ui.home.HomeScreen
import me.padamchopra.numbermatch.ui.onboarding.OnboardingScreen
import me.padamchopra.numbermatch.ui.splash.SplashScreen

@Composable
fun AppNavHost(
    controller: NavHostController,
    animationDuration: Int = 350,
) {
    NavHost(
        navController = controller,
        startDestination = when (getPlatform()) {
            Platform.Android -> Route.Auth
            Platform.IOS -> Route.SplashScreen
            Platform.Desktop -> Route.SplashScreen
        },
        enterTransition = {
            NavTransition.scaleInAnimation(animationDuration)
        },
        popEnterTransition = {
            NavTransition.scaleInAnimation(animationDuration)
        },
        exitTransition = {
            NavTransition.scaleOutAnimation(animationDuration)
        },
    ) {
        composable<Route.SplashScreen> {
            SplashScreen()
        }

        composable<Route.Auth>(
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
        ) {
            AuthScreen()
        }

        composable<Route.Home> {
            HomeScreen()
        }

        composable<Route.Onboarding> {
            OnboardingScreen()
        }
    }
}
