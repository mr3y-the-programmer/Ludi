package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.MMOGiveawayEntry
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.core.repository.query.DealsQuery
import com.mr3y.ludi.core.repository.query.DealsSorting
import com.mr3y.ludi.core.repository.query.DealsSortingDirection
import com.mr3y.ludi.core.repository.query.GiveawaysQueryParameters
import com.mr3y.ludi.core.repository.query.GiveawaysSorting
import com.mr3y.ludi.ui.presenter.model.DealsFiltersState
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.DealStore
import com.mr3y.ludi.ui.presenter.model.GiveawayPlatform
import com.mr3y.ludi.ui.presenter.model.GiveawayStore
import com.mr3y.ludi.ui.presenter.model.GiveawaysFiltersState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.wrapResultResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class DealsViewModel @Inject constructor(
    private val dealsRepository: DealsRepository
) : ViewModel() {

    private val searchQuery = mutableStateOf("")
    private val dealsFilterState = MutableStateFlow(InitialDealsFiltersState)
    private val isDealsLoading = MutableStateFlow(false)

    private val deals = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        dealsFilterState
    ) { searchText, dealsFiltersState ->
        isDealsLoading.update { true }
        dealsRepository.queryDeals(
            DealsQuery(
                searchQuery = if (searchText.isNotEmpty() && searchText.isNotBlank()) searchText else null,
                stores = if (dealsFiltersState.selectedStores.isNotEmpty()) dealsFiltersState.selectedStores.map { it.id } else null,
                sortingCriteria = dealsFiltersState.sortingCriteria,
                sortingDirection = dealsFiltersState.sortingDirection
            )
        ).single().also {
            isDealsLoading.update { false }
        }
    }

    private val isGiveawaysLoading = MutableStateFlow(false)
    private val giveawaysFiltersState = MutableStateFlow(InitialGiveawaysFiltersState)
    private val gamerPowerGiveaways = giveawaysFiltersState.mapLatest { giveawayFiltersState ->
        isGiveawaysLoading.update { true }
        dealsRepository.queryGamerPowerGiveaways(
            GiveawaysQueryParameters(
                platforms = giveawayFiltersState.selectedPlatformsAndStores(),
                sorting = giveawayFiltersState.sortingCriteria
            )
        ).single().also {
            isGiveawaysLoading.update { false }
        }
    }

    val dealsState = combine(
        snapshotFlow { searchQuery.value },
        dealsFilterState,
        deals,
        giveawaysFiltersState,
        gamerPowerGiveaways,
        dealsRepository.queryMMOGiveaways(),
        isDealsLoading,
        isGiveawaysLoading
    ) { updates ->
        val isDealsLoading = updates[6] as Boolean
        val isGiveawaysLoading = updates[7] as Boolean
        DealsState(
            searchQuery = updates[0] as String,
            dealsFiltersState = updates[1] as DealsFiltersState,
            deals = if (isDealsLoading) Initial.deals else (updates[2] as Result<List<Deal>, Throwable>).wrapResultResources(),
            giveawaysFiltersState = (updates[3] as GiveawaysFiltersState),
            otherGamesGiveaways = if (isGiveawaysLoading)
                Initial.otherGamesGiveaways
            else
                (updates[4] as Result<List<GamerPowerGiveawayEntry>, Throwable>).wrapResultResources(
                    transform = { giveaways ->
                        giveaways.filter { giveaway ->
                            giveaway.endDateTime?.isAfter(ZonedDateTime.now(ZoneId.systemDefault()))
                                ?: true
                        }
                    }),
            mmoGamesGiveaways = (updates[5] as Result<List<MMOGiveawayEntry>, Throwable>).wrapResultResources(),
        )
    }.stateIn(
        viewModelScope,
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

    private fun GiveawaysFiltersState.selectedPlatformsAndStores(): List<com.mr3y.ludi.core.repository.query.GiveawayPlatform>? {
        return when {
            selectedPlatforms.isNotEmpty() && selectedStores.isNotEmpty() -> {
                var temp = selectedStores.map { store ->
                    when(store) {
                        GiveawayStore.Steam -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Steam
                        GiveawayStore.EpicGames -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.EpicGames
                        GiveawayStore.Battlenet -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Battlenet
                        GiveawayStore.Ubisoft -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Ubisoft
                        GiveawayStore.Itchio -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Itchio
                        GiveawayStore.Origin -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Origin
                        GiveawayStore.GOG -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.GOG
                    }
                }
                temp = temp + selectedPlatforms.map { platform ->
                    when(platform) {
                        GiveawayPlatform.Android -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Android
                        GiveawayPlatform.IOS -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.IOS
                        GiveawayPlatform.Playstation4 -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Playstation4
                        GiveawayPlatform.Playstation5 -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Playstation5
                        GiveawayPlatform.XboxOne -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.XboxOne
                        GiveawayPlatform.Xbox360 -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Xbox360
                        GiveawayPlatform.XboxSeriesXs -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.XboxSeriesXs
                        GiveawayPlatform.NintendoSwitch -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.NintendoSwitch
                        GiveawayPlatform.PC -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.PC
                    }
                }
                return temp
            }
            selectedPlatforms.isNotEmpty() -> {
                selectedPlatforms.map { platform ->
                    when(platform) {
                        GiveawayPlatform.Android -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Android
                        GiveawayPlatform.IOS -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.IOS
                        GiveawayPlatform.Playstation4 -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Playstation4
                        GiveawayPlatform.Playstation5 -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Playstation5
                        GiveawayPlatform.XboxOne -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.XboxOne
                        GiveawayPlatform.Xbox360 -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Xbox360
                        GiveawayPlatform.XboxSeriesXs -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.XboxSeriesXs
                        GiveawayPlatform.NintendoSwitch -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.NintendoSwitch
                        GiveawayPlatform.PC -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.PC
                    }
                }
            }
            selectedStores.isNotEmpty() -> {
                selectedStores.map { store ->
                    when(store) {
                        GiveawayStore.Steam -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Steam
                        GiveawayStore.EpicGames -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.EpicGames
                        GiveawayStore.Battlenet -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Battlenet
                        GiveawayStore.Ubisoft -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Ubisoft
                        GiveawayStore.Itchio -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Itchio
                        GiveawayStore.Origin -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.Origin
                        GiveawayStore.GOG -> com.mr3y.ludi.core.repository.query.GiveawayPlatform.GOG
                    }
                }
            }
            else -> null
        }
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
            "",
            Result.Success(placeholderList()),
            Result.Success(placeholderList()),
            Result.Success(placeholderList()),
            InitialDealsFiltersState,
            InitialGiveawaysFiltersState
        )

        private fun placeholderList(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}