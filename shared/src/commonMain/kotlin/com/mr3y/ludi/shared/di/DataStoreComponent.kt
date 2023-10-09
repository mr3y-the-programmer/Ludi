package com.mr3y.ludi.shared.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.di.annotations.Singleton
import com.mr3y.ludi.shared.ui.datastore.FavouriteGamesSerializer
import com.mr3y.ludi.shared.ui.datastore.FavouriteGenresSerializer
import com.mr3y.ludi.shared.ui.datastore.FollowedNewsDataSourceSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import okio.FileSystem
import okio.Path

interface DataStoreComponent {

    @get:Provides
    val dataStoreParentDir: Path

    @Provides
    @Singleton
    fun provideFavouriteGamesDataStore(dataStoreParentDir: Path): DataStore<UserFavouriteGames> {
        return DataStoreFactory.create(
            storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FavouriteGamesSerializer, producePath = { dataStoreParentDir.resolve("datastore").resolve("fav_games.pb")}),
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Provides
    @Singleton
    fun provideFollowedNewsSourcesDataStore(dataStoreParentDir: Path): DataStore<FollowedNewsDataSources> {
        return DataStoreFactory.create(
            storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FollowedNewsDataSourceSerializer, producePath = { dataStoreParentDir.resolve("datastore").resolve("followed_news_sources.pb")}),
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteGenresDataStore(dataStoreParentDir: Path): DataStore<UserFavouriteGenres> {
        return DataStoreFactory.create(
            storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FavouriteGenresSerializer, producePath = { dataStoreParentDir.resolve("datastore").resolve("fav_genres.pb")}),
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )

    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(dataStoreParentDir: Path): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = PreferencesSerializer, producePath = { dataStoreParentDir.resolve("datastore").resolve("ludi_user.preferences_pb") }),
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}
