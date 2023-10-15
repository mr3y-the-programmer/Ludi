package com.mr3y.ludi.shared.ui.datastore.internal

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.ui.datastore.FavouriteGamesSerializer
import com.mr3y.ludi.shared.ui.datastore.FavouriteGenresSerializer
import com.mr3y.ludi.shared.ui.datastore.FollowedNewsDataSourceSerializer
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

@RunWith(AndroidJUnit4::class)
class DefaultProtoDataStoreMutatorTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources> = DataStoreFactory.create(
        storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FollowedNewsDataSourceSerializer, producePath = { tempFolder.newFolder().toOkioPath().resolve("datastore").resolve("followed_news_sources.pb") }),
        corruptionHandler = null,
        migrations = emptyList(),
        scope = testScope
    )
    private val favGamesStore: DataStore<UserFavouriteGames> = DataStoreFactory.create(
        storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FavouriteGamesSerializer, producePath = { tempFolder.newFolder().toOkioPath().resolve("datastore").resolve("fav_games.pb") }),
        corruptionHandler = null,
        migrations = emptyList(),
        scope = testScope
    )
    private val favGenresStore: DataStore<UserFavouriteGenres> = DataStoreFactory.create(
        storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FavouriteGenresSerializer, producePath = { tempFolder.newFolder().toOkioPath().resolve("datastore").resolve("fav_genres.pb") }),
        corruptionHandler = null,
        migrations = emptyList(),
        scope = testScope
    )

    private lateinit var sut: DefaultProtoDataStoreMutator

    @Before
    fun setUp() {
        sut = DefaultProtoDataStoreMutator(favGamesStore, followedNewsDataSourcesStore, favGenresStore)

        testScope.launch {
            followedNewsDataSourcesStore.updateData { it.copy(newsDataSource = emptyList()) }
            favGamesStore.updateData { it.copy(favGame = emptyList()) }
            favGenresStore.updateData { it.copy(favGenre = emptyList()) }
        }
    }

    @Test
    fun updates_to_datastore_preferences_are_idempotent() = testScope.runTest {
        // Initially expect that all preferences are empty
        sut.followedNewsDataSources.test {
            expectThat(awaitItem().newsDataSource).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
        sut.favouriteGames.test {
            expectThat(awaitItem().favGame).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
        sut.favouriteGenres.test {
            expectThat(awaitItem().favGenre).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        // make sure the function is idempotent & only adds unique values by type
        repeat(2) { sut.followNewsDataSource(FollowedNewsDataSource(name = "Game Spot", type = "GameSpot")) }
        repeat(2) { sut.followNewsDataSource(FollowedNewsDataSource(name = "Giant Bomb", type = "GameSpot")) }

        sut.followedNewsDataSources.test {
            val newsDataSource = awaitItem().newsDataSource.single()
            expectThat(newsDataSource).isEqualTo(FollowedNewsDataSource(name = "Game Spot", type = "GameSpot"))
            cancelAndIgnoreRemainingEvents()
        }

        // make sure the function is idempotent & only adds unique values
        repeat(2) { sut.addGameToFavourites(UserFavouriteGame(id = 34L, name = "DOTA 2", thumbnailUrl = "", rating = 4.52f)) }

        sut.favouriteGames.test {
            val game = awaitItem().favGame.single()
            expectThat(game).isEqualTo(UserFavouriteGame(id = 34L, name = "DOTA 2", thumbnailUrl = "", rating = 4.52f))
            cancelAndIgnoreRemainingEvents()
        }

        // make sure the function is idempotent & only adds unique values
        repeat(2) { sut.addGenreToFavourites(UserFavouriteGenre(id = 2, name = "Adventure", slug = "adventure", gamesCount = 2000, imageUrl = "https://media.rawg.io/media/games/f46/f466571d536f2e3ea9e815ad17177501.jpg")) }
        repeat(2) { sut.addGenreToFavourites(UserFavouriteGenre(id = 2, name = "Adventure", slug = "action", gamesCount = 4000, imageUrl = "")) }

        sut.favouriteGenres.test {
            val genre = awaitItem().favGenre.single()
            expectThat(genre).isEqualTo(UserFavouriteGenre(id = 2, name = "Adventure", slug = "adventure", gamesCount = 2000, imageUrl = "https://media.rawg.io/media/games/f46/f466571d536f2e3ea9e815ad17177501.jpg"))
            cancelAndIgnoreRemainingEvents()
        }

        // Also, make sure removing items is idempotent as well.
        repeat(2) { sut.unFollowNewsDataSource(FollowedNewsDataSource(name = "Game Spot", type = "GameSpot")) }
        repeat(2) { sut.removeGameFromFavourites(UserFavouriteGame(id = 34L, name = "DOTA 2", thumbnailUrl = "", rating = 4.52f)) }
        repeat(2) { sut.removeGenreFromFavourites(UserFavouriteGenre(id = 2, name = "Adventure", slug = "adventure", gamesCount = 2000, imageUrl = "https://media.rawg.io/media/games/f46/f466571d536f2e3ea9e815ad17177501.jpg")) }

        // eventually, expect that all preferences are empty
        sut.followedNewsDataSources.test {
            expectThat(awaitItem().newsDataSource).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
        sut.favouriteGames.test {
            expectThat(awaitItem().favGame).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
        sut.favouriteGenres.test {
            expectThat(awaitItem().favGenre).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun teardown() {
        testScope.launch {
            followedNewsDataSourcesStore.updateData { it.copy(newsDataSource = emptyList()) }
            favGamesStore.updateData { it.copy(favGame = emptyList()) }
            favGenresStore.updateData { it.copy(favGenre = emptyList()) }
        }
    }
}
