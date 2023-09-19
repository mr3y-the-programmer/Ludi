package com.mr3y.ludi.core.network.model

sealed interface ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>
    data class Error(val code: Int?, val throwable: Throwable? = null) : ApiResult<Nothing>
}
