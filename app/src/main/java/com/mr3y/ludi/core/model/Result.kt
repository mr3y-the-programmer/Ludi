package com.mr3y.ludi.core.model

sealed interface Result<out T, out E> {
    data class Success<T>(val data: T) : Result<T, Nothing>
    data class Error(val exception: Throwable? = null) : Result<Nothing, Throwable>
    object Loading : Result<Nothing, Nothing>
}

/**
 * [Success.data] if [Result] is of type [Success]
 */
fun <R, E> Result<R, E>.successOr(fallback: R): R {
    return (this as? Result.Success)?.data ?: fallback
}

val <R, E> Result<R, E>.data: R?
    get() = (this as? Result.Success)?.data

val <R, E> Result<R, E>.exception: Throwable?
    get() = (this as? Result.Error)?.exception