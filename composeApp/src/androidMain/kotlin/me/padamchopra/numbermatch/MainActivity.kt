package me.padamchopra.numbermatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val splashScreen = installSplashScreen()
        var keepSplashScreenVisible = true
        splashScreen.setKeepOnScreenCondition {
            keepSplashScreenVisible
        }

        setContent {
            val isInDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(isInDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                    ) {
                        isInDarkTheme
                    },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                    ) {
                        isInDarkTheme
                    },
                )
            }

            App(
                setSplashScreenVisible = {
                    keepSplashScreenVisible = it
                }
            )
        }
    }
}
