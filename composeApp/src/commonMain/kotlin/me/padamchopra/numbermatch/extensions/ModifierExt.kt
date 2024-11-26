package me.padamchopra.numbermatch.extensions

import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.debounceClickable(
    enabled: Boolean = true,
    debounceDuration: Long = 300,
    onClick: () -> Unit,
): Modifier = composed {
    var debounceActive by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(debounceActive) {
        if (debounceActive) {
            coroutineScope.launch {
                delay(debounceDuration)
                debounceActive = false
            }
        }
    }

    this.clickable(enabled && debounceActive.not()) {
        debounceActive = true
        onClick()
    }
}
