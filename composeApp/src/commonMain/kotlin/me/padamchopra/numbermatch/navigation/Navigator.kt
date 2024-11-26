package me.padamchopra.numbermatch.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object Navigator {
    private val _actionFlow: MutableSharedFlow<NavAction> = MutableSharedFlow()
    val actionFlow = _actionFlow.asSharedFlow()

    suspend fun execute(action: NavAction) {
        _actionFlow.emit(action)
    }
}
