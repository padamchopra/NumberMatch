package me.padamchopra.numbermatch.extensions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.debounceClickable(
    enabled: Boolean = true,
    debounceDuration: Long = 300,
    indication: Indication? = LocalIndication.current,
    onClick: () -> Unit,
): Modifier {
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

    return this.clickable(
        enabled = enabled && debounceActive.not(),
        indication = indication,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        debounceActive = true
        onClick()
    }
}

fun Modifier.noIndicationClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
) = this.clickable(
    enabled = enabled,
    interactionSource = null,
    indication = null,
    onClick = onClick
)

private enum class BounceState { Idle, Pressed }

@Composable
fun Modifier.bounceClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    var state by remember { mutableStateOf(BounceState.Idle) }
    val scale by animateFloatAsState(
        targetValue = if (state == BounceState.Pressed) .9f else 1f,
        label = "bounce click scale"
    )

    return this
        .scale(scale)
        .debounceClickable(
            enabled = enabled,
            indication = null,
            onClick = onClick
        )
        .pointerInput(state) {
            awaitPointerEventScope {
                state = if (state == BounceState.Pressed) {
                    waitForUpOrCancellation()
                    BounceState.Idle
                } else {
                    awaitFirstDown(false)
                    BounceState.Pressed
                }
            }
        }
}
