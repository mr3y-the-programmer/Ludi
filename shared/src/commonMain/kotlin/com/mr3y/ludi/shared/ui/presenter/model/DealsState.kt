package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.model.GiveawayEntry
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.repository.query.DealsSorting
import com.mr3y.ludi.shared.core.repository.query.DealsSortingDirection
import com.mr3y.ludi.shared.core.repository.query.GiveawaysSorting

data class DealsState(
    val deals: Result<List<Deal>, Throwable>,
    val giveaways: Result<List<GiveawayEntry>, Throwable>,
    val dealsFiltersState: DealsFiltersState,
    val giveawaysFiltersState: GiveawaysFiltersState,
    val selectedTab: Int,
    val showFilters: Boolean,
    val isRefreshingDeals: Boolean,
    val isRefreshingGiveaways: Boolean
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
