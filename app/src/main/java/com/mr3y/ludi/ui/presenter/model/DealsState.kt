package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.MMOGiveawayEntry
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.repository.query.DealsSorting
import com.mr3y.ludi.core.repository.query.DealsSortingDirection
import com.mr3y.ludi.core.repository.query.GiveawaysSorting

data class DealsState(
    val searchQuery: String,
    val deals: Result<List<ResourceWrapper<Deal>>, Throwable>,
    val mmoGamesGiveaways: Result<List<ResourceWrapper<MMOGiveawayEntry>>, Throwable>,
    val otherGamesGiveaways: Result<List<ResourceWrapper<GamerPowerGiveawayEntry>>, Throwable>,
    val dealsFiltersState: DealsFiltersState,
    val giveawaysFiltersState: GiveawaysFiltersState
)

data class DealsFiltersState(
    val currentPage: Int,
    val allStores: Set<DealStore>,
    val selectedStores: Set<DealStore>,
    val sortingCriteria: DealsSorting?,
    val sortingDirection: DealsSortingDirection?
)

data class DealStore(
    val id: Int,
    val label: String
)

data class GiveawaysFiltersState(
    val allPlatforms: Set<GiveawayPlatform>,
    val selectedPlatforms: Set<GiveawayPlatform>,
    val allStores: Set<GiveawayStore>,
    val selectedStores: Set<GiveawayStore>,
    val sortingCriteria: GiveawaysSorting?
)

enum class GiveawayPlatform {
    PC,
    Playstation4,
    Playstation5,
    XboxOne,
    XboxSeriesXs,
    Xbox360,
    Android,
    IOS,
    NintendoSwitch
}

enum class GiveawayStore {
    Steam,
    EpicGames,
    Ubisoft,
    GOG,
    Itchio,
    Origin,
    Battlenet
}
