package me.padamchopra.numbermatch.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class AppDimensions(
    /** 4.dp */
    val padding50: Dp = 4.dp,

    /** 8.dp */
    val padding100: Dp = 8.dp,

    /** 12.dp */
    val padding150: Dp = 12.dp,

    /** 16.dp */
    val padding200: Dp = 16.dp,

    /** 24.dp */
    val padding300: Dp = 24.dp,

    /** 32.dp */
    val padding400: Dp = 32.dp,

    val screenPadding: Dp = 16.dp,

    val icon75: Dp = 18.dp,
    val icon100: Dp = 24.dp,

    val button100: Dp = 48.dp,
)
