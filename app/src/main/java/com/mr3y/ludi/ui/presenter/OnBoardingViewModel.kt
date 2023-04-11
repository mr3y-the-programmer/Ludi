package com.mr3y.ludi.ui.presenter

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.FollowedNewsDataSource
import com.mr3y.ludi.FollowedNewsDataSources
import com.mr3y.ludi.R
import com.mr3y.ludi.UserFavouriteGame
import com.mr3y.ludi.UserFavouriteGames
import com.mr3y.ludi.copy
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.RichInfoGamesQuery
import com.mr3y.ludi.core.repository.query.RichInfoGamesSortingCriteria
import com.mr3y.ludi.ui.presenter.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class OnBoardingViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val favGamesStore: DataStore<UserFavouriteGames>,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>
) : ViewModel() {

    private val userFollowedNewsSources = followedNewsDataSourcesStore.data
        .catch {
            emit(FollowedNewsDataSources.getDefaultInstance())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.newsDataSourceList.map { followedDataSource ->
                NewsDataSource(name = followedDataSource.name, drawableId = followedDataSource.drawableId, type = Source.valueOf(followedDataSource.type))
            }
        }

    private val userFavGames = favGamesStore.data
        .catch {
            emit(UserFavouriteGames.getDefaultInstance())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.favGameList.map { favGame ->
                FavouriteGame(id = favGame.id, title = favGame.name, imageUrl = favGame.thumbnailUrl, rating = favGame.rating)
            }
        }

    private var _internalState by mutableStateOf(InitialOnboardingState)

    private var searchQuery = mutableStateOf("")

    private val allOnboardingGames = snapshotFlow { searchQuery.value }
        .debounce(275)
        .distinctUntilChanged()
        .flatMapLatest { searchText ->
            if (searchText.isEmpty()) {
                // Retrieve suggested games
                return@flatMapLatest gamesRepository.queryRichInfoGames(
                    RichInfoGamesQuery(
                        pageSize = 20,
                        dates = listOf(LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE), LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
                        sortingCriteria = RichInfoGamesSortingCriteria.DateAddedDescending
                    )
                ).map {
                    val result = when(it) {
                        is Result.Success -> Result.Success(it.data.games.map(RichInfoGame::wrapResource))
                        is Result.Error -> Result.Error(it.exception)
                    }
                    OnboardingGames.SuggestedGames(result)
                }
            }
            gamesRepository.queryRichInfoGames(
                RichInfoGamesQuery(
                    pageSize = 20,
                    searchQuery = searchText,
                    sortingCriteria = RichInfoGamesSortingCriteria.RatingDescending
                )
            ).map {
                val result = when(it) {
                    is Result.Success -> Result.Success(it.data.games.map(RichInfoGame::wrapResource))
                    is Result.Error -> Result.Error(it.exception)
                }
                OnboardingGames.SearchQueryBasedGames(result)
            }
        }

    val onboardingState: StateFlow<OnboardingState> = combine(
        userFollowedNewsSources,
        allOnboardingGames,
        userFavGames,
        snapshotFlow { _internalState }
    ) { followedNewsSources, onboardingGames, favouriteGames, state ->
        Snapshot.withMutableSnapshot {
            _internalState = state.copy(
                followedNewsDataSources = followedNewsSources,
                onboardingGames = onboardingGames,
                favouriteGames = favouriteGames
            )
        }
        _internalState
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            _internalState
        )

    fun unFollowNewsDataSource(source: NewsDataSource) {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFollowedNewsDataSources = true) }
        viewModelScope.launch {
            followedNewsDataSourcesStore.updateData {
                val sourceIndex = it.newsDataSourceList.indexOf(source.toFollowedNewsDataSource())
                // guard against cases where the user follows the source, and then unfollows it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeNewsDataSource(sourceIndex).build()
                } else
                    it
            }
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFollowedNewsDataSources = false) }
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFollowedNewsDataSources = true) }
        viewModelScope.launch {
            followedNewsDataSourcesStore.updateData {
                val followedNewsDataSource = source.toFollowedNewsDataSource()
                if (followedNewsDataSource !in it.newsDataSourceList) {
                    it.toBuilder().addNewsDataSource(followedNewsDataSource).build()
                } else
                    it
            }
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFollowedNewsDataSources = false) }
        }
    }

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
            _internalState = _internalState.copy(searchQuery = searchQueryText)
        }
    }

    fun addGameToFavourites(game: FavouriteGame) {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFavouriteGames = true) }
        viewModelScope.launch {
            favGamesStore.updateData {
                val favouriteGame = game.toUserFavouriteGame()
                if (favouriteGame !in it.favGameList) {
                    it.toBuilder().addFavGame(favouriteGame).build()
                } else
                    it
            }
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFavouriteGames = false) }
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFavouriteGames = true) }
        viewModelScope.launch {
            favGamesStore.updateData {
                val sourceIndex = it.favGameList.indexOf(game.toUserFavouriteGame())
                // guard against cases where the user adds the game to favourites, and then removes it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeFavGame(sourceIndex).build()
                } else
                    it
            }
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFavouriteGames = false) }
        }
    }

    companion object {
        private val SupportedNewsDataSources = listOf(
            NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot),
            NewsDataSource("Giant bomb", R.drawable.giant_bomb_logo, Source.GiantBomb),
            NewsDataSource("IGN", R.drawable.ign_logo, Source.IGN),
            NewsDataSource("MMO bomb", R.drawable.mmobomb_logo, Source.MMOBomb),
            NewsDataSource("Tech Radar", R.drawable.tech_radar_logo, Source.TechRadar),
        )
        val InitialOnboardingState = OnboardingState(
            bannerDrawablesIds = listOf(
                R.drawable.game1, R.drawable.game2,
                R.drawable.game3, R.drawable.game4,
                R.drawable.game5, R.drawable.game6,
                R.drawable.game7, R.drawable.game8,
                R.drawable.game9, R.drawable.game10,
                R.drawable.game11, R.drawable.game12,
                R.drawable.game13, R.drawable.game14,
                R.drawable.game15, R.drawable.game16,
                R.drawable.game17, R.drawable.game18,
                R.drawable.game19, R.drawable.game20,
                R.drawable.game21, R.drawable.game22,
                R.drawable.game23, R.drawable.game24
            ),
            allNewsDataSources = SupportedNewsDataSources,
            followedNewsDataSources = emptyList(),
            isUpdatingFollowedNewsDataSources = false,
            searchQuery = "",
            onboardingGames = OnboardingGames.SuggestedGames(Result.Success(listOf(ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder))),
            favouriteGames = emptyList(),
            isUpdatingFavouriteGames = false
        )
    }
}

private fun NewsDataSource.toFollowedNewsDataSource(): FollowedNewsDataSource {
    return FollowedNewsDataSource.newBuilder()
        .setName(name)
        .setDrawableId(drawableId)
        .setType(type.name)
        .build()
}

private fun FavouriteGame.toUserFavouriteGame(): UserFavouriteGame {
    return UserFavouriteGame.newBuilder()
        .setId(id)
        .setName(title)
        .setThumbnailUrl(imageUrl)
        .setRating(rating)
        .build()
}