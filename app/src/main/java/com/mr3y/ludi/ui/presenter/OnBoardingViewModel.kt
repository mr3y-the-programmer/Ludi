package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQuery
import com.mr3y.ludi.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.ui.datastore.PreferencesKeys
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.ui.presenter.model.OnboardingState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.SupportedNewsDataSources
import com.mr3y.ludi.ui.presenter.model.wrapResource
import com.mr3y.ludi.ui.presenter.model.wrapResultResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class OnBoardingViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val favGamesStore: DataStore<UserFavouriteGames>,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>,
    private val favGenresStore: DataStore<UserFavouriteGenres>,
    private val userPreferences: DataStore<Preferences>
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

    private val userFavGenres = favGenresStore.data
        .catch {
            emit(UserFavouriteGenres.getDefaultInstance())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.favGenreList.map { favGenre ->
                GameGenre(favGenre.id, favGenre.name, favGenre.slug, favGenre.gamesCount, favGenre.imageUrl)
            }
        }

    private var _internalState by mutableStateOf(InitialOnboardingState)

    private var searchQuery = mutableStateOf("")

    private val allOnboardingGames = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        userFavGenres
    ) { searchText, favouriteGenres ->
        if (searchText.isEmpty()) {
            // Retrieve suggested games
            return@combine gamesRepository.queryGames(
                GamesQuery(
                    pageSize = 20,
                    dates = listOf(LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE), LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
                    sortingCriteria = GamesSortingCriteria.DateAddedDescending,
                    genres = favouriteGenres.map { it.id }
                )
            ).let {
                val result = when (it) {
                    is Result.Success -> Result.Success(it.data.games.map(Game::wrapResource))
                    is Result.Error -> Result.Error(it.exception)
                }
                OnboardingGames.SuggestedGames(result)
            }
        }
        gamesRepository.queryGames(
            GamesQuery(
                pageSize = 20,
                searchQuery = searchText,
                sortingCriteria = GamesSortingCriteria.RatingDescending
            )
        ).let {
            val result = when (it) {
                is Result.Success -> Result.Success(it.data.games.map(Game::wrapResource))
                is Result.Error -> Result.Error(it.exception)
            }
            OnboardingGames.SearchQueryBasedGames(result)
        }
    }

    private val allGameGenres = flow {
        emit(gamesRepository.queryGamesGenres().wrapResultResources())
    }

    val onboardingState: StateFlow<OnboardingState> = combine(
        userFollowedNewsSources,
        allOnboardingGames,
        userFavGames,
        allGameGenres,
        userFavGenres,
        snapshotFlow { _internalState }
    ) {
        val followedNewsSources = it[0] as List<NewsDataSource>
        val onboardingGames = it[1] as OnboardingGames
        val favouriteGames = it[2] as List<FavouriteGame>
        val allGenres = it[3] as Result<ResourceWrapper<Set<GameGenre>>, Throwable>
        val favouriteGenres = it[4] as List<GameGenre>
        val state = it[5] as OnboardingState
        Snapshot.withMutableSnapshot {
            _internalState = state.copy(
                followedNewsDataSources = followedNewsSources,
                onboardingGames = onboardingGames,
                favouriteGames = favouriteGames,
                allGamingGenres = allGenres,
                selectedGamingGenres = favouriteGenres.toSet()
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
                } else {
                    it
                }
            }
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFollowedNewsDataSources = false) }
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFollowedNewsDataSources = true) }
        viewModelScope.launch {
            followedNewsDataSourcesStore.updateData {
                val followedNewsDataSource = source.toFollowedNewsDataSource()
                if (followedNewsDataSource.type !in it.newsDataSourceList.map { source -> source.type }) {
                    it.toBuilder().addNewsDataSource(followedNewsDataSource).build()
                } else {
                    it
                }
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
                } else {
                    it
                }
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
                } else {
                    it
                }
            }
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isUpdatingFavouriteGames = false) }
        }
    }

    fun selectGenre(genre: GameGenre) {
        viewModelScope.launch {
            favGenresStore.updateData {
                val favouriteGenre = genre.toUserFavouriteGenre()
                if (favouriteGenre !in it.favGenreList) {
                    it.toBuilder().addFavGenre(favouriteGenre).build()
                } else {
                    it
                }
            }
        }
    }

    fun unselectGenre(genre: GameGenre) {
        viewModelScope.launch {
            favGenresStore.updateData {
                val sourceIndex = it.favGenreList.indexOf(genre.toUserFavouriteGenre())
                // guard against cases where the user adds the genre to favourites, and then removes it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeFavGenre(sourceIndex).build()
                } else {
                    it
                }
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferences.edit { mutablePreferences ->
                mutablePreferences[PreferencesKeys.OnBoardingScreenKey] = false
            }
        }
    }

    companion object {
        val InitialOnboardingState = OnboardingState(
            allNewsDataSources = SupportedNewsDataSources,
            followedNewsDataSources = emptyList(),
            isUpdatingFollowedNewsDataSources = false,
            searchQuery = "",
            onboardingGames = OnboardingGames.SuggestedGames(Result.Success(listOf(ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder))),
            favouriteGames = emptyList(),
            isUpdatingFavouriteGames = false,
            allGamingGenres = Result.Success(ResourceWrapper.Placeholder),
            selectedGamingGenres = emptySet()
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

private fun GameGenre.toUserFavouriteGenre(): UserFavouriteGenre {
    return UserFavouriteGenre.newBuilder()
        .setId(id)
        .setName(name)
        .setSlug(slug)
        .setGamesCount(gamesCount ?: 0L)
        .setImageUrl(imageUrl)
        .build()
}
