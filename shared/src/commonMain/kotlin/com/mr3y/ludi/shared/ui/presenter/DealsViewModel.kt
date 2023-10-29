package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
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
import com.mr3y.ludi.shared.ui.presenter.model.DealsUiEvents
import com.mr3y.ludi.shared.ui.presenter.model.GiveawaysFiltersState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import me.tatarka.inject.annotations.Inject
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.coroutines.CoroutineContext

@Inject
class DealsViewModel(
    private val dealsRepository: DealsRepository
) : ScreenModel {

    val searchQuery = mutableStateOf("")

    private val events = MutableSharedFlow<DealsUiEvents>(extraBufferCapacity = 10)

    private val moleculeScope = CoroutineScope(coroutineScope.coroutineContext + frameClock())

    val dealsState = moleculeScope.launchMolecule(mode = RecompositionMode.ContextClock) {
        DealsPresenter(
            initialState = Initial,
            searchQueryState = searchQuery,
            events = events,
            dealsRepository = dealsRepository
        )
    }

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
        }
    }

    fun addToSelectedDealsStores(store: DealStore) {
        events.tryEmit(DealsUiEvents.AddToSelectedDealsStores(store = store))
    }

    fun removeFromSelectedDealsStores(store: DealStore) {
        events.tryEmit(DealsUiEvents.RemoveFromSelectedDealsStores(store = store))
    }

    fun addToSelectedGiveawaysStores(store: GiveawayStore) {
        events.tryEmit(DealsUiEvents.AddToSelectedGiveawaysStores(store = store))
    }

    fun removeFromSelectedGiveawaysStores(store: GiveawayStore) {
        events.tryEmit(DealsUiEvents.RemoveFromSelectedGiveawaysStores(store = store))
    }

    fun addToSelectedGiveawaysPlatforms(platform: GiveawayPlatform) {
        events.tryEmit(DealsUiEvents.AddToSelectedGiveawaysPlatforms(platform = platform))
    }

    fun removeFromSelectedGiveawayPlatforms(platform: GiveawayPlatform) {
        events.tryEmit(DealsUiEvents.RemoveFromSelectedGiveawaysPlatforms(platform = platform))
    }

    fun refreshDeals() {
        events.tryEmit(DealsUiEvents.RefreshDeals)
    }

    fun refreshGiveaways() {
        events.tryEmit(DealsUiEvents.RefreshGiveaways)
    }

    fun selectTab(tabIndex: Int) {
        events.tryEmit(DealsUiEvents.SelectTab(tabIndex))
    }

    fun toggleFilters() {
        events.tryEmit(DealsUiEvents.ToggleShowFilters)
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

internal expect fun frameClock(): CoroutineContext

@OptIn(FlowPreview::class)
@Composable
internal fun DealsPresenter(
    initialState: DealsState,
    searchQueryState: MutableState<String>,
    events: Flow<DealsUiEvents>,
    dealsRepository: DealsRepository
): DealsState {
    var selectedTab by remember { mutableStateOf(initialState.selectedTab) }
    var showFilters by remember { mutableStateOf(initialState.showFilters) }

    var isDealsLoading by remember { mutableStateOf(initialState.isRefreshingDeals) }
    var deals by remember { mutableStateOf(initialState.deals) }
    var dealsFilterState by remember { mutableStateOf(initialState.dealsFiltersState) }
    var refreshDeals by remember { mutableStateOf(0) }

    var isGiveawaysLoading by remember { mutableStateOf(initialState.isRefreshingGiveaways) }
    var giveaways by remember { mutableStateOf(initialState.giveaways) }
    var giveawaysFilterState by remember { mutableStateOf(initialState.giveawaysFiltersState) }
    var refreshGiveaways by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        snapshotFlow { searchQueryState.value }
            .debounce(275)
            .collectLatest { searchText ->
                isDealsLoading = true
                deals = dealsRepository.queryDeals(
                    DealsQuery(
                        searchQuery = if (searchText.isNotEmpty() && searchText.isNotBlank()) searchText else null,
                        stores = if (dealsFilterState.selectedStores.isNotEmpty()) dealsFilterState.selectedStores.map { it.id } else null,
                        sortingCriteria = dealsFilterState.sortingCriteria,
                        sortingDirection = dealsFilterState.sortingDirection
                    )
                )
                isDealsLoading = false
            }
    }

    LaunchedEffect(dealsFilterState, refreshDeals) {
        isDealsLoading = true
        deals = dealsRepository.queryDeals(
            DealsQuery(
                searchQuery = if (searchQueryState.value.isNotEmpty() && searchQueryState.value.isNotBlank()) searchQueryState.value else null,
                stores = if (dealsFilterState.selectedStores.isNotEmpty()) dealsFilterState.selectedStores.map { it.id } else null,
                sortingCriteria = dealsFilterState.sortingCriteria,
                sortingDirection = dealsFilterState.sortingDirection
            )
        )
        isDealsLoading = false
    }

    LaunchedEffect(giveawaysFilterState, refreshGiveaways) {
        isGiveawaysLoading = true
        giveaways = dealsRepository.queryGiveaways(
            GiveawaysQueryParameters(
                platforms = giveawaysFilterState.selectedPlatforms.toList(),
                stores = giveawaysFilterState.selectedStores.toList(),
                sorting = giveawaysFilterState.sortingCriteria
            )
        ).onSuccess { giveaways ->
            giveaways.filter { giveaway -> giveaway.endDateTime?.isAfter(ZonedDateTime.now(ZoneId.systemDefault())) ?: true }
        }
        isGiveawaysLoading = false
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is DealsUiEvents.AddToSelectedDealsStores -> dealsFilterState = dealsFilterState.copy(selectedStores = dealsFilterState.selectedStores + event.store)
                is DealsUiEvents.RemoveFromSelectedDealsStores -> dealsFilterState = dealsFilterState.copy(selectedStores = dealsFilterState.selectedStores - event.store)
                is DealsUiEvents.AddToSelectedGiveawaysStores -> giveawaysFilterState = giveawaysFilterState.copy(selectedStores = giveawaysFilterState.selectedStores + event.store)
                is DealsUiEvents.RemoveFromSelectedGiveawaysStores -> giveawaysFilterState = giveawaysFilterState.copy(selectedStores = giveawaysFilterState.selectedStores - event.store)
                is DealsUiEvents.AddToSelectedGiveawaysPlatforms -> giveawaysFilterState = giveawaysFilterState.copy(selectedPlatforms = giveawaysFilterState.selectedPlatforms + event.platform)
                is DealsUiEvents.RemoveFromSelectedGiveawaysPlatforms -> giveawaysFilterState = giveawaysFilterState.copy(selectedPlatforms = giveawaysFilterState.selectedPlatforms - event.platform)
                is DealsUiEvents.RefreshDeals -> refreshDeals++
                is DealsUiEvents.RefreshGiveaways -> refreshGiveaways++
                is DealsUiEvents.SelectTab -> selectedTab = event.tabIndex
                is DealsUiEvents.ToggleShowFilters -> showFilters = !showFilters
            }
        }
    }

    return DealsState(
        deals = deals,
        giveaways = giveaways,
        dealsFiltersState = dealsFilterState,
        giveawaysFiltersState = giveawaysFilterState,
        selectedTab = selectedTab,
        showFilters = showFilters,
        isRefreshingDeals = isDealsLoading,
        isRefreshingGiveaways = isGiveawaysLoading
    )
}
