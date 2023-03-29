package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.MMOGiveawayEntry
import com.mr3y.ludi.core.model.Result

data class DealsState(
    val deals: Result<List<ResourceWrapper<Deal>>, Throwable>,
    val mmoGamesGiveaways: Result<List<ResourceWrapper<MMOGiveawayEntry>>, Throwable>,
    val otherGamesGiveaways: Result<List<ResourceWrapper<GamerPowerGiveawayEntry>>, Throwable>
) {
    companion object {
        val Initial = DealsState(
            Result.Success(placeholderList()),
            Result.Success(placeholderList()),
            Result.Success(placeholderList())
        )

        private fun placeholderList(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}
