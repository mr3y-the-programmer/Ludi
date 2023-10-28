package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import app.cash.paging.PagingData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.model.GiveawayEntry
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.onSuccess
import com.mr3y.ludi.shared.core.repository.DealsRepository
import com.mr3y.ludi.shared.core.repository.query.DealsQuery
import com.mr3y.ludi.shared.core.repository.query.DealsSorting
import com.mr3y.ludi.shared.core.repository.query.DealsSortingDirection
import com.mr3y.ludi.shared.core.repository.query.GiveawayPlatform
import com.mr3y.ludi.shared.core.repository.query.GiveawayStore
import com.mr3y.ludi.shared.core.repository.query.GiveawaysQueryParameters
import com.mr3y.ludi.shared.core.repository.query.GiveawaysSorting
import com.mr3y.ludi.shared.ui.presenter.model.DealStore
import com.mr3y.ludi.shared.ui.presenter.model.DealsFiltersState
import com.mr3y.ludi.shared.ui.presenter.model.DealsState
import com.mr3y.ludi.shared.ui.presenter.model.GiveawaysFiltersState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(FlowPreview::class)
@Inject
class DealsViewModel(
    private val dealsRepository: DealsRepository
) : ScreenModel {

    val searchQuery = mutableStateOf("")
    private val dealsFilterState = MutableStateFlow(InitialDealsFiltersState)
    private val isDealsLoading = MutableStateFlow(false)

    private val _previousRefreshDealsValue = MutableStateFlow(0)
    private val refreshingDeals = MutableStateFlow(0)

    private val deals = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        dealsFilterState,
        refreshingDeals
    ) { searchText, dealsFiltersState, _ ->
        isDealsLoading.update { true }
        dealsRepository.queryDeals(
            DealsQuery(
                searchQuery = if (searchText.isNotEmpty() && searchText.isNotBlank()) searchText else null,
                stores = if (dealsFiltersState.selectedStores.isNotEmpty()) dealsFiltersState.selectedStores.map { it.id } else null,
                sortingCriteria = dealsFiltersState.sortingCriteria,
                sortingDirection = dealsFiltersState.sortingDirection
            )
        ).also {
            isDealsLoading.update { false }
            _previousRefreshDealsValue.update { refreshingDeals.value }
        }
    }

    private val isGiveawaysLoading = MutableStateFlow(false)
    private val giveawaysFiltersState = MutableStateFlow(InitialGiveawaysFiltersState)
    private val _previousRefreshGiveawaysValue = MutableStateFlow(0)
    private val refreshingGiveaways = MutableStateFlow(0)
    private val gamerPowerGiveaways = combine(giveawaysFiltersState, refreshingGiveaways) { giveawayFiltersState, _ ->
        isGiveawaysLoading.update { true }
        dealsRepository.queryGiveaways(
            GiveawaysQueryParameters(
                platforms = giveawayFiltersState.selectedPlatforms.toList(),
                stores = giveawayFiltersState.selectedStores.toList(),
                sorting = giveawayFiltersState.sortingCriteria
            )
        ).also {
            isGiveawaysLoading.update { false }
            _previousRefreshGiveawaysValue.update { refreshingGiveaways.value }
        }
    }

    private val selectedTab = MutableStateFlow(Initial.selectedTab)

    private val showFilters = MutableStateFlow(Initial.showFilters)

    @Suppress("UNCHECKED_CAST")
    val dealsState = combine(
        dealsFilterState,
        deals,
        giveawaysFiltersState,
        gamerPowerGiveaways,
        isDealsLoading,
        isGiveawaysLoading,
        refreshingDeals,
        _previousRefreshDealsValue,
        refreshingGiveaways,
        _previousRefreshGiveawaysValue,
        selectedTab,
        showFilters
    ) { updates ->
        val isDealsLoading = updates[4] as Boolean
        val isGiveawaysLoading = updates[5] as Boolean
        val refreshingDeals = updates[6] as Int
        val previousRefreshDeals = updates[7] as Int
        val refreshingGiveaways = updates[8] as Int
        val previousRefreshGiveaways = updates[9] as Int
        val selectedTabIndex = updates[10] as Int
        val isFiltersShown = updates[11] as Boolean
        DealsState(
            dealsFiltersState = updates[0] as DealsFiltersState,
            deals = updates[1] as Flow<PagingData<Deal>>,
            giveawaysFiltersState = (updates[2] as GiveawaysFiltersState),
            giveaways = if (isGiveawaysLoading) {
                Initial.giveaways
            } else {
                (updates[3] as Result<List<GiveawayEntry>, Throwable>).onSuccess { giveaways ->
                    giveaways.filter { giveaway -> giveaway.endDateTime?.isAfter(ZonedDateTime.now(ZoneId.systemDefault())) ?: true }
                }
            },
            isRefreshingDeals = refreshingDeals != previousRefreshDeals,
            isRefreshingGiveaways = refreshingGiveaways != previousRefreshGiveaways,
            selectedTab = selectedTabIndex,
            showFilters = isFiltersShown
        )
    }.stateIn(
        coroutineScope,
        SharingStarted.Lazily,
        Initial
    )

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
        }
    }

    fun addToSelectedDealsStores(store: DealStore) {
        dealsFilterState.update { it.copy(selectedStores = it.selectedStores + store) }
    }

    fun removeFromSelectedDealsStores(store: DealStore) {
        dealsFilterState.update { it.copy(selectedStores = it.selectedStores - store) }
    }

    fun addToSelectedGiveawaysStores(store: GiveawayStore) {
        giveawaysFiltersState.update { it.copy(selectedStores = it.selectedStores + store) }
    }

    fun removeFromSelectedGiveawaysStores(store: GiveawayStore) {
        giveawaysFiltersState.update { it.copy(selectedStores = it.selectedStores - store) }
    }

    fun addToSelectedGiveawaysPlatforms(platform: GiveawayPlatform) {
        giveawaysFiltersState.update { it.copy(selectedPlatforms = it.selectedPlatforms + platform) }
    }

    fun removeFromSelectedGiveawayPlatforms(platform: GiveawayPlatform) {
        giveawaysFiltersState.update { it.copy(selectedPlatforms = it.selectedPlatforms - platform) }
    }

    fun refreshDeals() {
        refreshingDeals.update { it + 1 }
    }

    fun refreshGiveaways() {
        refreshingGiveaways.update { it + 1 }
    }

    fun selectTab(tabIndex: Int) {
        selectedTab.update { tabIndex }
    }

    fun toggleFilters() {
        showFilters.update { !it }
    }

    companion object {
        val InitialDealsFiltersState = DealsFiltersState(
            currentPage = 0,
            allStores = setOf(
                DealStore(1, "Steam"),
                DealStore(2, "GamersGate"),
                DealStore(3, "GreenManGaming"),
                DealStore(7, "GOG"),
                DealStore(8, "Origin"),
                DealStore(11, "Humble Store"),
                DealStore(13, "Uplay"),
                DealStore(15, "Fanatical"),
                DealStore(21, "WinGameStore"),
                DealStore(23, "GameBillet"),
                DealStore(24, "Voidu"),
                DealStore(25, "Epic Games"),
                DealStore(27, "Games planet"),
                DealStore(28, "Games load"),
                DealStore(29, "2Game"),
                DealStore(30, "IndieGala"),
                DealStore(31, "Blizzard Shop"),
                DealStore(33, "DLGamer"),
                DealStore(34, "Noctre"),
                DealStore(35, "DreamGame")
            ).sortedBy { it.label }.toSet(),
            selectedStores = emptySet(),
            sortingCriteria = DealsSorting.Recent,
            sortingDirection = DealsSortingDirection.Descending
        )
        val InitialGiveawaysFiltersState = GiveawaysFiltersState(
            allPlatforms = GiveawayPlatform.values().toSortedSet(),
            selectedPlatforms = emptySet(),
            allStores = GiveawayStore.values().toSortedSet(),
            selectedStores = emptySet(),
            sortingCriteria = GiveawaysSorting.Popularity
        )
        val Initial = DealsState(
            emptyFlow(),
            Result.Loading,
            InitialDealsFiltersState,
            InitialGiveawaysFiltersState,
            isRefreshingDeals = true,
            isRefreshingGiveaways = true,
            selectedTab = 0,
            showFilters = false
        )
    }
}
