package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.onSuccess
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQuery
import com.mr3y.ludi.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.ui.datastore.PreferencesKeys
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.ui.presenter.model.OnboardingState
import com.mr3y.ludi.ui.presenter.model.SupportedNewsDataSources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(FlowPreview::class)
class OnBoardingViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val favGamesStore: DataStore<UserFavouriteGames>,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>,
    private val favGenresStore: DataStore<UserFavouriteGenres>,
    private val userPreferences: DataStore<Preferences>
) : ScreenModel {

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

    private val refreshingGames = MutableStateFlow(0)

    private val allOnboardingGames = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        userFavGenres,
        refreshingGames
    ) { searchText, favouriteGenres, _ ->
        if (searchText.isEmpty()) {
            // Retrieve suggested games
            return@combine gamesRepository.queryGames(
                GamesQuery(
                    pageSize = 20,
                    dates = listOf(LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE), LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
                    sortingCriteria = GamesSortingCriteria.DateAddedDescending,
                    genres = favouriteGenres.map { it.id }.takeIf { it.isNotEmpty() }
                )
            ).let {
                OnboardingGames.SuggestedGames(it.onSuccess { page -> page.games })
            }
        }
        gamesRepository.queryGames(
            GamesQuery(
                pageSize = 20,
                searchQuery = searchText,
                sortingCriteria = GamesSortingCriteria.RatingDescending
            )
        ).let {
            OnboardingGames.SearchQueryBasedGames(it.onSuccess { page -> page.games })
        }
    }.map {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshingGames = false, onboardingGames = it) }
    }.launchIn(coroutineScope)

    private val refreshingGenres = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val allGameGenres = refreshingGenres
        .mapLatest {
            gamesRepository.queryGamesGenres().onSuccess { page -> page.genres }
        }.map {
            Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshingGenres = false, allGamingGenres = it) }
        }.launchIn(coroutineScope)

    val onboardingState: StateFlow<OnboardingState> = combine(
        userFollowedNewsSources,
        userFavGames,
        userFavGenres,
        snapshotFlow { _internalState }
    ) { followedNewsSources, favouriteGames, favouriteGenres, state ->
        Snapshot.withMutableSnapshot {
            _internalState = state.copy(
                followedNewsDataSources = followedNewsSources,
                favouriteGames = favouriteGames,
                selectedGamingGenres = favouriteGenres.toSet()
            )
        }
        _internalState
    }
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            _internalState
        )

    fun unFollowNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            followedNewsDataSourcesStore.updateData {
                val sourceIndex = it.newsDataSourceList.indexOf(source.toFollowedNewsDataSource())
                // guard against cases where the user follows the source, and then unfollows it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeNewsDataSource(sourceIndex).build()
                } else {
                    it
                }
            }
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            followedNewsDataSourcesStore.updateData {
                val followedNewsDataSource = source.toFollowedNewsDataSource()
                if (followedNewsDataSource.type !in it.newsDataSourceList.map { source -> source.type }) {
                    it.toBuilder().addNewsDataSource(followedNewsDataSource).build()
                } else {
                    it
                }
            }
        }
    }

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
            _internalState = _internalState.copy(searchQuery = searchQueryText)
        }
    }

    fun addGameToFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            favGamesStore.updateData {
                val favouriteGame = game.toUserFavouriteGame()
                if (favouriteGame !in it.favGameList) {
                    it.toBuilder().addFavGame(favouriteGame).build()
                } else {
                    it
                }
            }
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            favGamesStore.updateData {
                val sourceIndex = it.favGameList.indexOf(game.toUserFavouriteGame())
                // guard against cases where the user adds the game to favourites, and then removes it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeFavGame(sourceIndex).build()
                } else {
                    it
                }
            }
        }
    }

    fun selectGenre(genre: GameGenre) {
        coroutineScope.launch {
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
        coroutineScope.launch {
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

    fun refreshGames() {
        refreshingGames.update { it + 1 }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshingGames = true) }
    }

    fun refreshGenres() {
        refreshingGenres.update { it + 1 }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshingGenres = true) }
    }

    fun completeOnboarding() {
        coroutineScope.launch {
            userPreferences.edit { mutablePreferences ->
                mutablePreferences[PreferencesKeys.OnBoardingScreenKey] = false
            }
        }
    }

    companion object {
        val InitialOnboardingState = OnboardingState(
            allNewsDataSources = SupportedNewsDataSources,
            followedNewsDataSources = emptyList(),
            searchQuery = "",
            onboardingGames = OnboardingGames.SuggestedGames(Result.Loading),
            isRefreshingGames = true,
            favouriteGames = emptyList(),
            allGamingGenres = Result.Loading,
            isRefreshingGenres = true,
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
