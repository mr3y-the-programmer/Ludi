package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGamesGenresPage

sealed interface ResourceWrapper<out T> {
    object Placeholder : ResourceWrapper<Nothing>
    data class ActualResource<T>(val resource: T) : ResourceWrapper<T>
}

val <T> ResourceWrapper<T>.actualResource: T?
    get() = (this as? ResourceWrapper.ActualResource)?.resource

internal fun <T : Any> T.wrapResource() = ResourceWrapper.ActualResource(this)

internal fun <T : Any> Result<Iterable<T>, Throwable>.wrapResultResources(): Result<List<ResourceWrapper<T>>, Throwable> {
    return when (this) {
        is Result.Success -> Result.Success(data.map { it.wrapResource() })
        is Result.Error -> this
    }
}

internal fun <T : Any, R : Any> Result<Iterable<T>, Throwable>.wrapResultResources(transform: (Iterable<T>) -> Iterable<R>): Result<List<ResourceWrapper<R>>, Throwable> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data).map { it.wrapResource() })
        is Result.Error -> this
    }
}

@JvmName("wrapGenresInResultResources")
fun Result<RichInfoGamesGenresPage, Throwable>.wrapResultResources(): Result<ResourceWrapper<Set<GameGenre>>, Throwable> {
    return when (this) {
        is Result.Success -> Result.Success(ResourceWrapper.ActualResource(data.genres))
        is Result.Error -> this
    }
}
