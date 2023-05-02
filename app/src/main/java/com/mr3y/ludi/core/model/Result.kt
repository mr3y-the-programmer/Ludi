package com.mr3y.ludi.core.model

import com.slack.eithernet.ApiResult

sealed interface Result<out T, out E> {
    data class Success<T>(val data: T) : Result<T, Nothing>
    data class Error(val exception: Throwable? = null) : Result<Nothing, Throwable>
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

fun <E : Any> ApiResult.Failure<E>.toCoreErrorResult(): Result.Error {
    return when (this) {
        is ApiResult.Failure.NetworkFailure -> Result.Error(error)
        is ApiResult.Failure.HttpFailure -> Result.Error()
        is ApiResult.Failure.ApiFailure -> Result.Error()
        is ApiResult.Failure.UnknownFailure -> Result.Error(error)
    }
}
