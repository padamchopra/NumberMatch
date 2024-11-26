package me.padamchopra.numbermatch.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class AppColors(
    val blue: Color = Color(0xFF0056A3),
    val red: Color = Color(0xFFD71920),
    val black: Color = Color(0xFF000000),
    val yellow: Color = Color(0xFFF9D616),
    val tileBackground: Color = Color(0xFFFAF3E0), // or Color(0xFFFFF9E6)
    val success: Color = Color(0xFF34C759),
    val error: Color = Color(0xFFFF3B30)
)
