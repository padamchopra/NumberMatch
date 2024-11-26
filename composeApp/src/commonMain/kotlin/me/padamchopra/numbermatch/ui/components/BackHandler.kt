package me.padamchopra.numbermatch.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
)
