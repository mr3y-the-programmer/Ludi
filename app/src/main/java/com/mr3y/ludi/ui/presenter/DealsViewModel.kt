package com.mr3y.ludi.ui.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.*
import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.UiResult
import com.mr3y.ludi.ui.presenter.model.wrapResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val dealsRepository: DealsRepository
) : ViewModel() {

    val dealsState = combine(
        dealsRepository.queryDeals(),
        dealsRepository.queryMMOGiveaways(),
        dealsRepository.queryGamerPowerGiveaways()
    ) { dealsResult, mmoGiveawaysResult, gamerPowerGiveawaysResult ->
        DealsState(
            deals = dealsResult.mapToUiResult(DealsState.Initial.deals),
            mmoGamesGiveaways = mmoGiveawaysResult.mapToUiResult(DealsState.Initial.mmoGamesGiveaways),
            otherGamesGiveaways = gamerPowerGiveawaysResult.mapToUiResult(DealsState.Initial.otherGamesGiveaways),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        DealsState.Initial
    )
}

@JvmName("DealsMapToUiResult")
private fun Result<List<Deal>, Throwable>.mapToUiResult(
    initialValue: UiResult<List<ResourceWrapper<Deal>>, Throwable>
): UiResult<List<ResourceWrapper<Deal>>, Throwable> {
    return when(this) {
        is Result.Loading -> initialValue
        is Result.Success -> UiResult.Content(data.map(Deal::wrapResource))
        is Result.Error -> UiResult.Error(exception)
    }
}

@JvmName("MMOGiveawayEntriesMapToUiResult")
private fun Result<List<MMOGiveawayEntry>, Throwable>.mapToUiResult(
    initialValue: UiResult<List<ResourceWrapper<MMOGiveawayEntry>>, Throwable>
): UiResult<List<ResourceWrapper<MMOGiveawayEntry>>, Throwable> {
    return when(this) {
        is Result.Loading -> initialValue
        is Result.Success -> UiResult.Content(data.map(MMOGiveawayEntry::wrapResource))
        is Result.Error -> UiResult.Error(exception)
    }
}

@JvmName("GamerPowerGiveawayEntriesMapToUiResult")
private fun Result<List<GamerPowerGiveawayEntry>, Throwable>.mapToUiResult(
    initialValue: UiResult<List<ResourceWrapper<GamerPowerGiveawayEntry>>, Throwable>
): UiResult<List<ResourceWrapper<GamerPowerGiveawayEntry>>, Throwable> {
    return when(this) {
        is Result.Loading -> initialValue
        is Result.Success -> UiResult.Content(data.map(GamerPowerGiveawayEntry::wrapResource))
        is Result.Error -> UiResult.Error(exception)
    }
}