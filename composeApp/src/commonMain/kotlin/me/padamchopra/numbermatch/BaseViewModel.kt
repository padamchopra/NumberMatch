package me.padamchopra.numbermatch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.padamchopra.numbermatch.extensions.getErrorMessage
import me.padamchopra.numbermatch.utils.ShowSnackBar
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KProperty1

abstract class BaseViewModel<S: UiState>(initialState: S): ViewModel() {
    private val _state = MutableStateFlow(initialState)
    protected val stateFlow = _state.asStateFlow()
    private val mutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T> onAsync(
        asyncProp: KProperty1<S, Async<T>>,
        onFail: (suspend (Exception) -> Unit)? = { e ->
            ShowSnackBar.error(e.getErrorMessage())
        },
        onSuccess: (suspend (T) -> Unit)? = null
    ) {
        viewModelScope.launch {
            stateFlow
                .drop(1)
                .mapLatest {
                    asyncProp.get(it)
                }
                .distinctUntilChanged()
                .collectLatest {
                    when (it) {
                        is Async.Fail -> onFail?.invoke(it.e)
                        is Async.Success -> onSuccess?.invoke(it.data)
                        else -> {
                            // no-op
                        }
                    }
                }
        }
    }

    @Composable
    fun collectAsState() = _state.collectAsState()

    protected fun withState(block: (S) -> Unit) {
        block(_state.value)
    }

    protected fun setState(reducer: S.() -> S) {
        viewModelScope.launch {
            mutex.withLock {
                _state.update { it.reducer() }
            }
        }
    }

    protected open fun <T : Any?> (suspend () -> T).execute(
        dispatcher: CoroutineDispatcher? = null,
        reducer: S.(Async<T>) -> S
    ): Job {
        return viewModelScope.launch(dispatcher ?: EmptyCoroutineContext) {
            setState { reducer(Async.Loading) }
            try {
                val result = invoke()
                setState { reducer(Async.Success(result)) }
            } catch (e: Exception) {
                setState { reducer(Async.Fail(e)) }
            }
        }
    }
}
