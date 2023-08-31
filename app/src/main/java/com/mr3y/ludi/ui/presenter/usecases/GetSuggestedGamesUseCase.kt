package com.mr3y.ludi.ui.presenter.usecases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import com.mr3y.ludi.LudiAppState
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.onSuccess
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQuery
import com.mr3y.ludi.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.TaggedGames
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface GetSuggestedGamesUseCase {
    suspend operator fun invoke(page: Int): DiscoverStateGames.SuggestedGames
}

class GetSuggestedGamesUseCaseImpl @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val appState: LudiAppState
) : GetSuggestedGamesUseCase {

    private var loadedGames: MutableState<DiscoverStateGames.SuggestedGames?> = mutableStateOf(null)

    override suspend operator fun invoke(page: Int): DiscoverStateGames.SuggestedGames {
        val suggestedGames = coroutineScope {
            when (page) {
                0 -> {
                    val trendingGames = async {
                        fetchTaggedGames(
                            dates = listOf(
                                LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE),
                                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                            ),
                            metaCriticScores = listOf(85, 100),
                            sortingCriteria = GamesSortingCriteria.DateUpdatedDescending,
                            result = TaggedGames::TrendingGames
                        )
                    }
                    val topRatedGames = async {
                        fetchTaggedGames(metaCriticScores = listOf(95, 100), result = TaggedGames::TopRatedGames)
                    }
                    val multiplayerGames = async {
                        fetchTaggedGames(genres = listOf(59), result = TaggedGames::MultiplayerGames)
                    }
                    listOf(
                        trendingGames,
                        topRatedGames,
                        multiplayerGames
                    ).awaitAll().let {
                        DiscoverStateGames.SuggestedGames(taggedGamesList = it)
                    }.also {
                        Snapshot.withMutableSnapshot {
                            loadedGames.value = it
                        }
                    }
                }
                1 -> {
                    val newLoadedGames = mutableListOf<Deferred<TaggedGames>>()
                    val favouriteGenresIds = appState.userFavouriteGenresIds
                    if (!favouriteGenresIds.isNullOrEmpty()) {
                        newLoadedGames += async {
                            fetchTaggedGames(genres = favouriteGenresIds, result = TaggedGames::BasedOnFavGenresGames)
                        }
                    }
                    newLoadedGames += async {
                        fetchTaggedGames(tags = listOf(79), result = TaggedGames::FreeGames)
                    }
                    updateAndGetLoadedGames(newLoadedGames.awaitAll())
                }
                2 -> {
                    val storyGames = async {
                        fetchTaggedGames(tags = listOf(406), result = TaggedGames::StoryGames)
                    }
                    val boardGames = async {
                        fetchTaggedGames(tags = listOf(162), result = TaggedGames::BoardGames)
                    }
                    updateAndGetLoadedGames(listOf(storyGames, boardGames).awaitAll())
                }
                3 -> {
                    val eSportsGames = async {
                        fetchTaggedGames(tags = listOf(73), result = TaggedGames::ESportsGames)
                    }
                    val raceGames = async {
                        fetchTaggedGames(tags = listOf(1407), result = TaggedGames::RaceGames)
                    }
                    updateAndGetLoadedGames(listOf(eSportsGames, raceGames).awaitAll())
                }
                4 -> {
                    val puzzleGames = async {
                        fetchTaggedGames(tags = listOf(83, 1867), result = TaggedGames::PuzzleGames)
                    }

                    val soundtrackGames = async {
                        fetchTaggedGames(tags = listOf(42), result = TaggedGames::SoundtrackGames)
                    }
                    updateAndGetLoadedGames(listOf(puzzleGames, soundtrackGames).awaitAll())
                }
                else -> loadedGames.value!!
            }
        }
        return suggestedGames
    }

    private suspend fun <T : TaggedGames> fetchTaggedGames(
        genres: List<Int>? = null,
        tags: List<Int>? = null,
        metaCriticScores: List<Int>? = null,
        dates: List<String>? = null,
        result: (Result<List<Game>, Throwable>) -> T,
        sortingCriteria: GamesSortingCriteria = GamesSortingCriteria.RatingDescending
    ): T {
        return gamesRepository.queryGames(
            GamesQuery(
                pageSize = 20,
                tags = tags,
                genres = genres,
                dates = dates,
                metaCriticScores = metaCriticScores,
                sortingCriteria = sortingCriteria
            )
        ).onSuccess { gamesPage ->
            gamesPage.games
        }.let {
            result(it)
        }
    }

    private fun updateAndGetLoadedGames(newGames: List<TaggedGames>): DiscoverStateGames.SuggestedGames {
        Snapshot.withMutableSnapshot {
            loadedGames.value = DiscoverStateGames.SuggestedGames(
                loadedGames.value!!.taggedGamesList.plus(newGames)
            )
        }
        return loadedGames.value!!
    }
}
