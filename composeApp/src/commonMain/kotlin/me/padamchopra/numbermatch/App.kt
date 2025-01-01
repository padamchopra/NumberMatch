package me.padamchopra.numbermatch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.padamchopra.numbermatch.di.dataSourcesModule
import me.padamchopra.numbermatch.di.platformModule
import me.padamchopra.numbermatch.di.repositoriesModule
import me.padamchopra.numbermatch.di.utilsModule
import me.padamchopra.numbermatch.di.viewModelsModule
import me.padamchopra.numbermatch.navigation.AppNavHost
import me.padamchopra.numbermatch.navigation.NavAction
import me.padamchopra.numbermatch.ui.AppTheme
import me.padamchopra.numbermatch.ui.CustomSnackBarHost
import me.padamchopra.numbermatch.utils.AppLogger
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    setSplashScreenVisible: (Boolean) -> Unit = {},
) {
    val navController = rememberNavController()
    val snackBarHostState = remember { CustomSnackBarHost() }

    AppTheme {
        KoinApplication(
            application = {
                modules(
                    platformModule(),
                    dataSourcesModule(),
                    repositoriesModule(),
                    utilsModule(),
                    viewModelsModule()
                )
            }
        ) {
            val viewModel: AppViewModel = koinViewModel<AppViewModel>()
            val state by viewModel.collectAsState()

            LaunchedEffect(state.keepSplashScreenVisible) {
                setSplashScreenVisible(state.keepSplashScreenVisible)
            }

            LaunchedEffect(state.navAction) {
                state.navAction?.let { action ->
                    AppLogger.debug("Nav Action", action.toString())

                    when (action) {
                        is NavAction.ClearStackAndPush<*> -> {
                            navController.navigate(action.route) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        is NavAction.GoTo<*> -> {
                            navController.navigate(action.route) {
                                launchSingleTop = true
                            }
                        }
                        NavAction.Pop -> {
                            navController.popBackStack()
                        }
                    }
                    viewModel.onNavActionHandled()
                }
            }

            LaunchedEffect(state.snackBarData) {
                state.snackBarData?.let { data ->
                    val result = snackBarHostState.showSnackbar(data)
                    when (result) {
                        SnackbarResult.Dismissed -> {
                            // Do nothing
                        }
                        SnackbarResult.ActionPerformed -> {
                            data.onAction?.invoke()
                        }
                    }
                    viewModel.onSnackBarDataHandled()
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                AppNavHost(
                    controller = navController,
                )
                CustomSnackBarHost(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .navigationBarsPadding(),
                    hostState = snackBarHostState,
                )
            }
        }
    }
}