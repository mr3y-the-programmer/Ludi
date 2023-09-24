package com.mr3y.ludi.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.di.annotations.Singleton
import com.mr3y.ludi.ui.datastore.FavouriteGamesSerializer
import com.mr3y.ludi.ui.datastore.FavouriteGenresSerializer
import com.mr3y.ludi.ui.datastore.FollowedNewsDataSourceSerializer
import me.tatarka.inject.annotations.Provides

interface DataStoreComponent {

    @Provides
    @Singleton
    fun provideFavouriteGamesDataStore(applicationContext: Context): DataStore<UserFavouriteGames> {
        return DataStoreFactory.create(
            serializer = FavouriteGamesSerializer,
            produceFile = { applicationContext.dataStoreFile("fav_games.pb") }
        )
    }

    @Provides
    @Singleton
    fun provideFollowedNewsSourcesDataStore(applicationContext: Context): DataStore<FollowedNewsDataSources> {
        return DataStoreFactory.create(
            serializer = FollowedNewsDataSourceSerializer,
            produceFile = { applicationContext.dataStoreFile("followed_news_sources.pb") }
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteGenresDataStore(applicationContext: Context): DataStore<UserFavouriteGenres> {
        return DataStoreFactory.create(
            serializer = FavouriteGenresSerializer,
            produceFile = { applicationContext.dataStoreFile("fav_genres.pb") }
        )
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(applicationContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create { applicationContext.preferencesDataStoreFile("ludi_user") }
    }
}
