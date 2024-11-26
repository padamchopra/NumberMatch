package me.padamchopra.numbermatch

sealed class Async<out T>(private val value: T?) {
    data object Uninitialized : Async<Nothing>(null)
    data object Loading : Async<Nothing>(null)
    data class Success<T>(val data: T) : Async<T>(data)
    data class Fail(val e: Exception) : Async<Nothing>(null)

    val isLoadingOrSuccess by lazy { this is Loading || this is Success }

    fun <O> mapSuccess(transform: (T) -> O): Async<O> {
        return when (this) {
            is Uninitialized -> Uninitialized
            is Loading -> Loading
            is Success -> Success(transform(data))
            is Fail -> this
        }
    }
}
