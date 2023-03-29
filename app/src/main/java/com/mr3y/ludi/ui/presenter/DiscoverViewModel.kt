package com.mr3y.ludi.ui.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.*
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.wrapResource
import com.mr3y.ludi.ui.presenter.model.wrapResultResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    val discoverState = combine(
        gamesRepository.queryFreeGames(FreeGamesQueryParameters(platform = FreeGamesPlatform.pc, sortingCriteria = FreeGamesSortingCriteria.relevance, categories = null)),
        gamesRepository.queryRichInfoGames(
            RichInfoGamesQueryParameters(
                pageSize = 20,
                dates = listOf(LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE), LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
                metaCriticScores = listOf(85, 100),
                sortingCriteria = RichInfoGamesSortingCriteria.ReleasedDescending,
                page = null,
                searchQuery = null,
                isFuzzinessEnabled = null,
                matchTermsExactly = null,
                parentPlatforms = null,
                platforms = null,
                stores = null,
                developers = null,
                genres = null,
                tags = null,
            )
        ),
        gamesRepository.queryRichInfoGames(
            RichInfoGamesQueryParameters(
                pageSize = 20,
                metaCriticScores = listOf(95, 100),
                sortingCriteria = RichInfoGamesSortingCriteria.RatingDescending,
                page = null,
                searchQuery = null,
                isFuzzinessEnabled = null,
                matchTermsExactly = null,
                parentPlatforms = null,
                platforms = null,
                stores = null,
                developers = null,
                genres = null,
                tags = null,
                dates = null
            )
        )
    ) { freeGamesResult, trendingGamesResult, topRatedGamesResult ->
        DiscoverState(
            freeGames = freeGamesResult.wrapResultResources(),
            trendingGames = trendingGamesResult.wrapResultResources(),
            topRatedGames = topRatedGamesResult.wrapResultResources(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        DiscoverState.Initial
    )
}

private fun Result<RichInfoGamesPage, Throwable>.wrapResultResources(): Result<List<ResourceWrapper<RichInfoGame>>, Throwable> {
    return when(this) {
        is Result.Success -> Result.Success(data.games.map(RichInfoGame::wrapResource))
        is Result.Error -> this
    }
}
