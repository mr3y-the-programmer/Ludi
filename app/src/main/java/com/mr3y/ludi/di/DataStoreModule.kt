package com.mr3y.ludi.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mr3y.ludi.FollowedNewsDataSources
import com.mr3y.ludi.UserFavouriteGames
import com.mr3y.ludi.ui.datastore.FavouriteGamesSerializer
import com.mr3y.ludi.ui.datastore.FollowedNewsDataSourceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideFavouriteGamesDataStore(@ApplicationContext context: Context): DataStore<UserFavouriteGames> {
        return DataStoreFactory.create(
            serializer = FavouriteGamesSerializer,
            produceFile = { context.dataStoreFile("fav_games.pb") }
        )
    }

    @Provides
    @Singleton
    fun provideFollowedNewsSourcesDataStore(@ApplicationContext context: Context): DataStore<FollowedNewsDataSources> {
        return DataStoreFactory.create(
            serializer = FollowedNewsDataSourceSerializer,
            produceFile = { context.dataStoreFile("followed_news_sources.pb") }
        )
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("ludi_user") }
    }
}
