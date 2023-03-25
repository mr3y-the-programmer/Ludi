package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.RichInfoGame

sealed interface UiResult<out T, out E> {
    data class Content<T>(val data: T) : UiResult<T, Nothing>

    data class Error(val exception: Throwable? = null) : UiResult<Nothing, Throwable>
}

sealed interface ResourceWrapper<out T> {
    object Placeholder : ResourceWrapper<Nothing>
    data class ActualResource<T>(val resource: T) : ResourceWrapper<T>
}

val <T> ResourceWrapper<T>.actualResource: T?
    get() = (this as? ResourceWrapper.ActualResource)?.resource

internal fun <T: Any> T.wrapResource() = ResourceWrapper.ActualResource(this)

data class DiscoverState(
    val freeGames: UiResult<List<ResourceWrapper<FreeGame>>, Throwable>,
    val trendingGames: UiResult<List<ResourceWrapper<RichInfoGame>>, Throwable>,
    val topRatedGames: UiResult<List<ResourceWrapper<RichInfoGame>>, Throwable>
) {
    companion object {
        val Initial = DiscoverState(
            UiResult.Content(placeholderList()),
            UiResult.Content(placeholderList()),
            UiResult.Content(placeholderList())
        )

        private fun placeholderList(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}
