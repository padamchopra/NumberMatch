package me.padamchopra.numbermatch.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object NavTransition {
    fun scaleOutAnimation(
        animationDuration: Int = 500,
    ): ExitTransition {
        return scaleOut(
            animationSpec = tween(animationDuration),
            targetScale = 0.6f,
        )
    }

    fun scaleInAnimation(
        animationDuration: Int = 500,
    ): EnterTransition {
        return scaleIn(
            animationSpec = tween(animationDuration),
            initialScale = 0.6f,
        )
    }

    fun slideInFromBottomAnimation(
        animationDuration: Int = 500,
    ): EnterTransition {
        return slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(animationDuration),
        )
    }

    fun slideInLeftAnimation(
        animationDuration: Int = 500,
    ): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(animationDuration),
        )
    }

    fun slideOutToBottomAnimation(
        animationDuration: Int = 500,
    ): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(animationDuration),
        )
    }

    fun slideOutRightAnimation(
        animationDuration: Int = 500,
    ): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(animationDuration),
        )
    }
}
