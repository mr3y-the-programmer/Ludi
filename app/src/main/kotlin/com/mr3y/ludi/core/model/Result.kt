package com.mr3y.ludi.core.model

import com.mr3y.ludi.core.network.model.ApiResult

sealed interface Result<out T, out E> {
    data object Loading : Result<Nothing, Nothing>
    data class Success<T>(val data: T) : Result<T, Nothing>
    data class Error(val exception: Throwable? = null) : Result<Nothing, Throwable>
}

/**
 * Allow transforming [Result.Success.data] if [Result] is [Result.Success] or return whatever result is otherwise.
 */
inline fun <T, R, E> Result<T, E>.onSuccess(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Loading -> this
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
    }
}

val <R, E> Result<R, E>.data: R?
    get() = (this as? Result.Success)?.data

val <R, E> Result<R, E>.exception: Throwable?
    get() = (this as? Result.Error)?.exception

fun ApiResult.Error.toCoreErrorResult(): Result.Error {
    return Result.Error(throwable)
}
