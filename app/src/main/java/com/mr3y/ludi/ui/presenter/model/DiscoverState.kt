package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
data class DiscoverState(
    val freeGames: Result<List<ResourceWrapper<FreeGame>>, Throwable>,
    val trendingGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
    val topRatedGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>
) {
    companion object {
        val Initial = DiscoverState(
            Result.Success(placeholderList()),
            Result.Success(placeholderList()),
            Result.Success(placeholderList())
        )

        private fun placeholderList(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}
