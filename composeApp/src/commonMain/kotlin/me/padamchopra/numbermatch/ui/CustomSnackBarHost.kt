package me.padamchopra.numbermatch.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFilterNotNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapTo
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.padamchopra.numbermatch.models.SnackBarData
import kotlin.coroutines.resume

@Stable
class CustomSnackBarHost {
    private val mutex = Mutex()
    var currentSnackBarData by mutableStateOf<CustomSnackBarData?>(null)
        private set

    @Stable
    class CustomSnackBarData(
        override val visuals: SnackbarVisuals,
        val type: SnackBarData.Type,
        private val continuation: CancellableContinuation<SnackbarResult>,
    ) : SnackbarData {

        override fun performAction() {
            if (continuation.isActive) continuation.resume(SnackbarResult.ActionPerformed)
        }

        override fun dismiss() {
            if (continuation.isActive) continuation.resume(SnackbarResult.Dismissed)
        }
    }

    data class CustomSnackbarVisuals(
        override val message: String,
        override val actionLabel: String?,
        override val withDismissAction: Boolean,
        override val duration: SnackbarDuration,
    ) : SnackbarVisuals

    suspend fun showSnackbar(data: SnackBarData): SnackbarResult =
        mutex.withLock {
            val message = data.message.resolve()
            val actionLabel = data.actionLabel?.resolve()
            try {
                return suspendCancellableCoroutine { continuation ->
                    currentSnackBarData = CustomSnackBarData(
                        visuals = CustomSnackbarVisuals(
                            message = message,
                            actionLabel = actionLabel,
                            withDismissAction = data.onAction != null,
                            duration = if (data.actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long
                        ),
                        type = data.type,
                        continuation = continuation,
                    )
                }
            } finally {
                currentSnackBarData = null
            }
        }
}

@Composable
fun CustomSnackBarHost(
    modifier: Modifier = Modifier,
    hostState: CustomSnackBarHost,
) {
    val currentSnackBarData = hostState.currentSnackBarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackBarData) {
        if (currentSnackBarData != null) {
            val duration = currentSnackBarData.visuals.duration.toMillis(
                currentSnackBarData.visuals.withDismissAction,
                accessibilityManager
            )
            delay(duration)
            currentSnackBarData.dismiss()
        }
    }
    FadeInFadeOutWithScale(
        current = hostState.currentSnackBarData,
        modifier = modifier,
        content = {
            CustomSnackBar(data = it)
        }
    )
}


@Composable
private fun CustomSnackBar(
    data: CustomSnackBarHost.CustomSnackBarData,
) {
    val backgroundColor = MaterialTheme.colorScheme.onSurface
        .copy(alpha = 0.8f)
        .compositeOver(MaterialTheme.colorScheme.surface)
    var height by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    Row(
        modifier = Modifier
            .padding(MaterialTheme.dimensions.padding150)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(backgroundColor)
            .onSizeChanged {
                height = with(density) {
                    it.height.toDp()
                }
            },
    ) {
        if (data.type != SnackBarData.Type.Default) {
            Box(
                modifier = Modifier
                    .width(MaterialTheme.dimensions.padding150)
                    .height(height)
                    .background(
                        when (data.type) {
                            SnackBarData.Type.Success -> MaterialTheme.appColors.success
                            SnackBarData.Type.Error -> MaterialTheme.appColors.error
                            else -> Color.Transparent
                        }
                    ),
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.dimensions.padding150),
            text = data.visuals.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surface,
        )

        if (data.visuals.actionLabel != null) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                onClick = data::performAction,
                content = {
                    Text(
                        text = data.visuals.actionLabel.orEmpty(),
                    )
                }
            )
        }
    }
}

private fun SnackbarDuration.toMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?
): Long {
    val original = when (this) {
        SnackbarDuration.Indefinite -> Long.MAX_VALUE
        SnackbarDuration.Long -> 10000L
        SnackbarDuration.Short -> 4000L
    }
    if (accessibilityManager == null) {
        return original
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        original,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction
    )
}

@Composable
private fun FadeInFadeOutWithScale(
    current: CustomSnackBarHost.CustomSnackBarData?,
    modifier: Modifier = Modifier,
    content: @Composable (CustomSnackBarHost.CustomSnackBarData) -> Unit
) {
    val state = remember { FadeInFadeOutState<CustomSnackBarHost.CustomSnackBarData?>() }
    if (current != state.current) {
        state.current = current
        val keys = state.items.fastMap { it.key }.toMutableList()
        if (!keys.contains(current)) {
            keys.add(current)
        }
        state.items.clear()
        keys.fastFilterNotNull().fastMapTo(state.items) { key ->
            FadeInFadeOutAnimationItem(key) { children ->
                val isVisible = key == current
                val duration = if (isVisible) SnackbarFadeInMillis else SnackbarFadeOutMillis
                val delay = SnackbarFadeOutMillis + SnackbarInBetweenDelayMillis
                val animationDelay = if (isVisible && keys.fastFilterNotNull().size != 1) {
                    delay
                } else {
                    0
                }
                val opacity = animatedOpacity(
                    animation = tween(
                        easing = LinearEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration
                    ),
                    visible = isVisible,
                    onAnimationFinish = {
                        if (key != state.current) {
                            // leave only the current in the list
                            state.items.removeAll { it.key == key }
                            state.scope?.invalidate()
                        }
                    }
                )
                val scale = animatedScale(
                    animation = tween(
                        easing = FastOutSlowInEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration
                    ),
                    visible = isVisible
                )
                Box(
                    Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = opacity.value
                        )
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                            dismiss { key.dismiss(); true }
                        }
                ) {
                    children()
                }
            }
        }
    }
    Box(modifier) {
        state.scope = currentRecomposeScope
        state.items.fastForEach { (item, opacity) ->
            key(item) {
                opacity {
                    content(item!!)
                }
            }
        }
    }
}

private class FadeInFadeOutState<T> {
    // we use Any here as something which will not be equals to the real initial value
    var current: Any? = Any()
    var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
    var scope: RecomposeScope? = null
}

private data class FadeInFadeOutAnimationItem<T>(
    val key: T,
    val transition: FadeInFadeOutTransition
)

@Composable
private fun animatedOpacity(
    animation: AnimationSpec<Float>,
    visible: Boolean,
    onAnimationFinish: () -> Unit = {}
): State<Float> {
    val alpha = remember { Animatable(if (!visible) 1f else 0f) }
    LaunchedEffect(visible) {
        alpha.animateTo(
            if (visible) 1f else 0f,
            animationSpec = animation
        )
        onAnimationFinish()
    }
    return alpha.asState()
}

@Composable
private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean): State<Float> {
    val scale = remember { Animatable(if (!visible) 1f else 0.8f) }
    LaunchedEffect(visible) {
        scale.animateTo(
            if (visible) 1f else 0.8f,
            animationSpec = animation
        )
    }
    return scale.asState()
}

private const val SnackbarFadeInMillis = 150
private const val SnackbarFadeOutMillis = 75
private const val SnackbarInBetweenDelayMillis = 0


private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit
