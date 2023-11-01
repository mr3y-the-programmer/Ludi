package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.model.mapTypeToIconRes
import com.mr3y.ludi.shared.core.model.onSuccess
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.shared.core.repository.query.GamesQuery
import com.mr3y.ludi.shared.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.shared.ui.datastore.PreferencesKeys
import com.mr3y.ludi.shared.ui.datastore.ProtoDataStoreMutator
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.shared.ui.presenter.model.OnboardingState
import com.mr3y.ludi.shared.ui.presenter.model.SupportedNewsDataSources
import com.mr3y.ludi.shared.ui.presenter.model.toFollowedNewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.toUserFavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.toUserFavouriteGenre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(FlowPreview::class)
@Inject
class OnBoardingViewModel(
    private val gamesRepository: GamesRepository,
    private val protoDataStore: ProtoDataStoreMutator,
    private val userPreferences: DataStore<Preferences>
) : ScreenModel {

    private val userFollowedNewsSources = protoDataStore.followedNewsDataSources
        .catch {
            emit(FollowedNewsDataSources())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.newsDataSource.map { followedDataSource ->
                val type = Source.valueOf(followedDataSource.type)
                NewsDataSource(name = followedDataSource.name, type = type, iconResName = mapTypeToIconRes(type))
            }
        }

    private val userFavGames = protoDataStore.favouriteGames
        .catch {
            emit(UserFavouriteGames())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.favGame.map { favGame ->
                FavouriteGame(id = favGame.id, title = favGame.name, imageUrl = favGame.thumbnailUrl, rating = favGame.rating)
            }
        }

    private val userFavGenres = protoDataStore.favouriteGenres
        .catch {
            emit(UserFavouriteGenres())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.favGenre.map { favGenre ->
                GameGenre(favGenre.id, favGenre.name, favGenre.slug, favGenre.gamesCount, favGenre.imageUrl)
            }
        }

    private var _internalState by mutableStateOf(InitialOnboardingState)

    val searchQuery = mutableStateOf("")

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
            val pagingData = gamesRepository.queryGames(
                GamesQuery(
                    dates = listOf(LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE), LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
                    sortingCriteria = GamesSortingCriteria.DateAddedDescending,
                    genres = favouriteGenres.map { it.id }.takeIf { it.isNotEmpty() }
                )
            ).cachedIn(coroutineScope)

            return@combine OnboardingGames.SuggestedGames(games = pagingData)
        }
        val searchPagingData = gamesRepository.queryGames(
            GamesQuery(
                searchQuery = searchText,
                sortingCriteria = GamesSortingCriteria.RatingDescending
            )
        )
        OnboardingGames.SearchQueryBasedGames(games = searchPagingData)
    }.map {
        _internalState = _internalState.copy(isRefreshingGames = false, onboardingGames = it)
    }.launchIn(coroutineScope)

    private val refreshingGenres = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val allGameGenres = refreshingGenres
        .mapLatest {
            gamesRepository.queryGamesGenres().onSuccess { page -> page.genres }
        }.map {
            _internalState = _internalState.copy(isRefreshingGenres = false, allGamingGenres = it)
        }.launchIn(coroutineScope)

    val onboardingState: StateFlow<OnboardingState> = combine(
        userFollowedNewsSources,
        userFavGames,
        userFavGenres,
        snapshotFlow { _internalState }
    ) { followedNewsSources, favouriteGames, favouriteGenres, state ->
        _internalState = state.copy(
            followedNewsDataSources = followedNewsSources,
            favouriteGames = favouriteGames,
            selectedGamingGenres = favouriteGenres.toSet()
        )
        _internalState
    }
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            _internalState
        )

    fun unFollowNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            protoDataStore.unFollowNewsDataSource(source.toFollowedNewsDataSource())
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            protoDataStore.followNewsDataSource(source.toFollowedNewsDataSource())
        }
    }

    fun updateSearchQuery(searchQueryText: String) {
        searchQuery.value = searchQueryText
    }

    fun addGameToFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            protoDataStore.addGameToFavourites(game.toUserFavouriteGame())
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            protoDataStore.removeGameFromFavourites(game.toUserFavouriteGame())
        }
    }

    fun selectGenre(genre: GameGenre) {
        coroutineScope.launch {
            protoDataStore.addGenreToFavourites(genre.toUserFavouriteGenre())
        }
    }

    fun unselectGenre(genre: GameGenre) {
        coroutineScope.launch {
            protoDataStore.removeGenreFromFavourites(genre.toUserFavouriteGenre())
        }
    }

    fun refreshGames() {
        refreshingGames.update { it + 1 }
        _internalState = _internalState.copy(isRefreshingGames = true)
    }

    fun refreshGenres() {
        refreshingGenres.update { it + 1 }
        _internalState = _internalState.copy(isRefreshingGenres = true)
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
            onboardingGames = OnboardingGames.SuggestedGames(games = emptyFlow()),
            isRefreshingGames = true,
            favouriteGames = emptyList(),
            allGamingGenres = Result.Loading,
            isRefreshingGenres = true,
            selectedGamingGenres = emptySet()
        )
    }
}
