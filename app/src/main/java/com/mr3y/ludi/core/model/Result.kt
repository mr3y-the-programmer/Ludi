package com.mr3y.ludi.core.model

import com.slack.eithernet.ApiResult

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

fun <E : Any> ApiResult.Failure<E>.toCoreErrorResult(): Result.Error {
    return when (this) {
        is ApiResult.Failure.NetworkFailure -> Result.Error(error)
        is ApiResult.Failure.HttpFailure -> Result.Error()
        is ApiResult.Failure.ApiFailure -> Result.Error()
        is ApiResult.Failure.UnknownFailure -> Result.Error(error)
    }
}
