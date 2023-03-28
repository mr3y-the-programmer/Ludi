package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.MMOGiveawayEntry

data class DealsState(
    val deals: UiResult<List<ResourceWrapper<Deal>>, Throwable>,
    val mmoGamesGiveaways: UiResult<List<ResourceWrapper<MMOGiveawayEntry>>, Throwable>,
    val otherGamesGiveaways: UiResult<List<ResourceWrapper<GamerPowerGiveawayEntry>>, Throwable>
) {
    companion object {
        val Initial = DealsState(
            UiResult.Content(placeholderList()),
            UiResult.Content(placeholderList()),
            UiResult.Content(placeholderList())
        )

        private fun placeholderList(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}
