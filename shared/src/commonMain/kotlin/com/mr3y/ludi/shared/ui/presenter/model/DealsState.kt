package com.mr3y.ludi.shared.ui.presenter.model

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.model.GiveawayEntry
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.repository.query.DealsSorting
import com.mr3y.ludi.shared.core.repository.query.DealsSortingDirection
import com.mr3y.ludi.shared.core.repository.query.GiveawayPlatform
import com.mr3y.ludi.shared.core.repository.query.GiveawayStore
import com.mr3y.ludi.shared.core.repository.query.GiveawaysSorting
import kotlinx.coroutines.flow.Flow

data class DealsState(
    val deals: Flow<PagingData<Deal>>,
    val giveaways: Result<List<GiveawayEntry>, Throwable>,
    val dealsFiltersState: DealsFiltersState,
    val giveawaysFiltersState: GiveawaysFiltersState,
    val selectedTab: Int,
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

sealed interface DealsUiEvents {

    data class AddToSelectedDealsStores(val store: DealStore) : DealsUiEvents

    data class RemoveFromSelectedDealsStores(val store: DealStore) : DealsUiEvents

    data class AddToSelectedGiveawaysStores(val store: GiveawayStore) : DealsUiEvents

    data class RemoveFromSelectedGiveawaysStores(val store: GiveawayStore) : DealsUiEvents

    data class AddToSelectedGiveawaysPlatforms(val platform: GiveawayPlatform) : DealsUiEvents

    data class RemoveFromSelectedGiveawaysPlatforms(val platform: GiveawayPlatform) : DealsUiEvents

    data object RefreshDeals : DealsUiEvents

    data object RefreshGiveaways : DealsUiEvents

    data class SelectTab(val tabIndex: Int) : DealsUiEvents
}
