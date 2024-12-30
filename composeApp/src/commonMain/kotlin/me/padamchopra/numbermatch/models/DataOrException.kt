package me.padamchopra.numbermatch.models

sealed class DataOrException<T> {
    data class Data<T>(val data: T) : DataOrException<T>()
    data class Exception<T>(val exception: Throwable?) : DataOrException<T>() {
        val safeException by lazy { exception ?: Exception("Unknown error occurred") }
    }

    fun unwrap(): T {
        return when (this) {
            is Data -> data
            is Exception -> throw safeException
        }
    }
}
