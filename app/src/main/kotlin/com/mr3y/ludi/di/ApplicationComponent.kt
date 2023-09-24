package com.mr3y.ludi.di

import android.content.Context
import com.mr3y.ludi.LudiApplication
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@Singleton
abstract class ApplicationComponent(
    @get:Provides val applicationContext: Context
) : CoroutinesModule,
    DataStoreModule,
    LoggingModule,
    NetworkModule,
    RepositoriesModule,
    RESTfulDataSourcesModule,
    RSSFeedDataSourcesModule,
    UseCasesModule {

    companion object {
        fun from(context: Context): ApplicationComponent {
            return (context.applicationContext as LudiApplication).component
        }
    }
}