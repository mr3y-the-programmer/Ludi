package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.Result

sealed interface ResourceWrapper<out T> {
    object Placeholder : ResourceWrapper<Nothing>
    data class ActualResource<T>(val resource: T) : ResourceWrapper<T>
}

val <T> ResourceWrapper<T>.actualResource: T?
    get() = (this as? ResourceWrapper.ActualResource)?.resource

internal fun <T: Any> T.wrapResource() = ResourceWrapper.ActualResource(this)

internal fun <T: Any> Result<Iterable<T>, Throwable>.wrapResultResources(): Result<List<ResourceWrapper<T>>, Throwable> {
    return when(this) {
        is Result.Success -> Result.Success(data.map { it.wrapResource() })
        is Result.Error -> this
    }
}