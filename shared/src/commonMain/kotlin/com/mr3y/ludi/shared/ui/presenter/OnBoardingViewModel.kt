package com.mr3y.ludi.shared.ui.presenter

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
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.model.onSuccess
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.shared.core.repository.query.GamesQuery
import com.mr3y.ludi.shared.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.ui.datastore.PreferencesKeys
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.shared.ui.presenter.model.OnboardingState
import com.mr3y.ludi.shared.ui.presenter.model.SupportedNewsDataSources
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
import me.tatarka.inject.annotations.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(FlowPreview::class)
@Inject
class OnBoardingViewModel(
    private val gamesRepository: GamesRepository,
    private val favGamesStore: DataStore<UserFavouriteGames>,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>,
    private val favGenresStore: DataStore<UserFavouriteGenres>,
    private val userPreferences: DataStore<Preferences>
) : ScreenModel {

    private val userFollowedNewsSources = followedNewsDataSourcesStore.data
        .catch {
            emit(FollowedNewsDataSources())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.newsDataSource.map { followedDataSource ->
                NewsDataSource(name = followedDataSource.name, drawableId = followedDataSource.drawableId, type = Source.valueOf(followedDataSource.type), iconResName = mapTypeToIconRes(Source.valueOf(followedDataSource.type)))
            }
        }

    private val userFavGames = favGamesStore.data
        .catch {
            emit(UserFavouriteGames())
            // TODO: materialize the error by showing snackbar or something "Couldn't save your favourite choice"
        }
        .map {
            it.favGame.map { favGame ->
                FavouriteGame(id = favGame.id, title = favGame.name, imageUrl = favGame.thumbnailUrl, rating = favGame.rating)
            }
        }

    private val userFavGenres = favGenresStore.data
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
                it.copy(newsDataSource = it.newsDataSource - source.toFollowedNewsDataSource())
            }
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            followedNewsDataSourcesStore.updateData {
                val followedNewsDataSource = source.toFollowedNewsDataSource()
                if (followedNewsDataSource.type !in it.newsDataSource.map { source -> source.type }) {
                    it.copy(newsDataSource = it.newsDataSource + followedNewsDataSource)
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
                if (favouriteGame !in it.favGame) {
                    it.copy(favGame = it.favGame + favouriteGame)
                } else {
                    it
                }
            }
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            favGamesStore.updateData {
                it.copy(favGame = it.favGame - game.toUserFavouriteGame())
            }
        }
    }

    fun selectGenre(genre: GameGenre) {
        coroutineScope.launch {
            favGenresStore.updateData {
                val favouriteGenre = genre.toUserFavouriteGenre()
                if (favouriteGenre.name !in it.favGenre.map { genre -> genre.name }) {
                    it.copy(favGenre = it.favGenre + favouriteGenre)
                } else {
                    it
                }
            }
        }
    }

    fun unselectGenre(genre: GameGenre) {
        coroutineScope.launch {
            favGenresStore.updateData {
                it.copy(favGenre = it.favGenre - genre.toUserFavouriteGenre())
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

    private fun mapTypeToIconRes(type: Source): String {
        return when(type) {
            Source.GiantBomb -> "giant_bomb_logo.xml"
            Source.GameSpot -> "game_spot_logo.xml"
            Source.IGN -> "ign_logo.xml"
            Source.TechRadar -> "tech_radar_logo.xml"
            Source.PCGamesN -> "pcgamesn_logo.xml"
            Source.RockPaperShotgun -> "rockpapershotgun_logo.xml"
            Source.PCInvasion -> "pcinvasion_logo.xml"
            Source.GloriousGaming -> "gloriousgaming_logo.xml"
            Source.EuroGamer -> "eurogamer_logo.xml"
            Source.VG247 -> "vg247_logo.xml"
            Source.TheGamer -> "thegamer_logo.xml"
            Source.GameRant -> "gamerant_logo.xml"
            Source.BrutalGamer -> "brutalgamer_logo.xml"
            Source.VentureBeat -> "venturebeat_logo.xml"
            Source.Polygon -> "polygon_logo.xml"
            Source.PCGamer -> "pcgamer_logo.xml"
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
    return FollowedNewsDataSource(name = name, drawableId = drawableId ?: 0, type = type.name)
}

private fun FavouriteGame.toUserFavouriteGame(): UserFavouriteGame {
    return UserFavouriteGame(id = id, name = title, thumbnailUrl = imageUrl, rating = rating)
}

private fun GameGenre.toUserFavouriteGenre(): UserFavouriteGenre {
    return UserFavouriteGenre(id = id, name = name, imageUrl = imageUrl ?: "", slug = slug ?: "", gamesCount = gamesCount ?: 0L)
}
