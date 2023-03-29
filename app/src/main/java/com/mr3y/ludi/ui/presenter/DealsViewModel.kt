package com.mr3y.ludi.ui.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.wrapResultResources
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
            deals = dealsResult.wrapResultResources(),
            mmoGamesGiveaways = mmoGiveawaysResult.wrapResultResources(),
            otherGamesGiveaways = gamerPowerGiveawaysResult.wrapResultResources(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        DealsState.Initial
    )
}
