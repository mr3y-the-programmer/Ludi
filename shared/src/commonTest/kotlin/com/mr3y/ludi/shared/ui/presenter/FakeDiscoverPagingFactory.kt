package com.mr3y.ludi.shared.ui.presenter

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.repository.fixtures.FakeGamesRepository
import com.mr3y.ludi.shared.core.repository.query.GamesQuery
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverFiltersState
import kotlinx.coroutines.flow.Flow

class FakeDiscoverPagingFactory(private val repo: FakeGamesRepository) : DiscoverPagingFactory {
    override val trendingGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val topRatedGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val favGenresBasedGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val multiplayerGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val freeGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val storyGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val boardGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val esportsGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val raceGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val puzzleGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override val soundtrackGamesPager: Flow<PagingData<Game>> = repo.queryGames(GamesQuery())

    override fun searchQueryBasedGamesPager(
        searchQuery: String,
        filters: DiscoverFiltersState
    ): Flow<PagingData<Game>> {
        return repo.queryGames(GamesQuery(searchQuery = searchQuery))
    }
}
