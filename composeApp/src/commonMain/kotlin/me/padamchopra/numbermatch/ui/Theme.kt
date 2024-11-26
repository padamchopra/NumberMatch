package me.padamchopra.numbermatch.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.plusjakartasans_bold
import numbermatch.composeapp.generated.resources.plusjakartasans_extrabold
import numbermatch.composeapp.generated.resources.plusjakartasans_light
import numbermatch.composeapp.generated.resources.plusjakartasans_medium
import numbermatch.composeapp.generated.resources.plusjakartasans_regular
import numbermatch.composeapp.generated.resources.plusjakartasans_semibold
import org.jetbrains.compose.resources.Font

private val LocalAppColors = staticCompositionLocalOf {
    AppColors()
}

private val LocalDimensions = staticCompositionLocalOf {
    AppDimensions()
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val fontFamily = FontFamily(
        Font(Res.font.plusjakartasans_extrabold, FontWeight.ExtraBold),
        Font(Res.font.plusjakartasans_bold, FontWeight.Bold),
        Font(Res.font.plusjakartasans_semibold, FontWeight.SemiBold),
        Font(Res.font.plusjakartasans_regular, FontWeight.Normal),
        Font(Res.font.plusjakartasans_light, FontWeight.Light),
        Font(Res.font.plusjakartasans_medium, FontWeight.Medium),
    )
    val colorScheme = if (isSystemInDarkTheme) {
        darkColorScheme(
            primary = Color(0xFF1B5A7A),
        )
    } else {
        lightColorScheme(
            background = Color(0xFFF9F9F9),
            primary = Color(0xFF1B5A7A),
            surfaceContainerLow = Color(0xFFFFFFFF),
            surfaceContainerHighest = Color(0xFFFFFFFF),
            inverseSurface = Color(0xFF000000),
            inverseOnSurface = Color(0xFFFFFFFF),
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            displayLarge = MaterialTheme.typography.displayLarge.copy(
                fontFamily = fontFamily,
            ),
            displayMedium = MaterialTheme.typography.displayMedium.copy(
                fontFamily = fontFamily,
            ),
            displaySmall = MaterialTheme.typography.displaySmall.copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
            ),
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = fontFamily,
            ),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = fontFamily,
            ),
            headlineSmall = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = fontFamily,
            ),
            titleLarge = MaterialTheme.typography.titleLarge.copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
            titleMedium = MaterialTheme.typography.titleMedium.copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.sp,
            ),
            titleSmall = MaterialTheme.typography.titleSmall.copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
            ),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = fontFamily,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
            ),
            bodyMedium = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = fontFamily,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
            ),
            bodySmall = MaterialTheme.typography.bodySmall.copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
            ),
            labelLarge = MaterialTheme.typography.labelLarge.copy(
                fontFamily = fontFamily,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            labelMedium = MaterialTheme.typography.labelMedium.copy(
                fontFamily = fontFamily,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
            ),
            labelSmall = MaterialTheme.typography.labelSmall.copy(
                fontFamily = fontFamily,
                fontSize = 8.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
            ),
        ),
        content = {
            CompositionLocalProvider(LocalDimensions provides AppDimensions()) {
                CompositionLocalProvider(LocalAppColors provides AppColors()) {
                    content()
                }
            }
        },
    )
}

val Color.high
    get() = copy(alpha = 0.8f)

val Color.medium
    get() = copy(alpha = 0.5f)

val Color.low
    get() = copy(alpha = 0.3f)

val Color.disabled
    get() = copy(alpha = 0.3f)

val Color.veryLow
    get() = copy(alpha = 0.12f)

val Shapes.rounded
    get() = RoundedCornerShape(100)

val MaterialTheme.appColors: AppColors
    @Composable
    get() = LocalAppColors.current

val MaterialTheme.dimensions: AppDimensions
    @Composable
    get() = LocalDimensions.current
