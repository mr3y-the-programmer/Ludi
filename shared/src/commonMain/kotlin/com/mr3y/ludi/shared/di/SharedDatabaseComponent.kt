package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.core.database.DriverFactory
import com.mr3y.ludi.shared.core.database.LudiDatabase
import com.mr3y.ludi.shared.core.database.createDatabase
import com.mr3y.ludi.shared.core.database.dao.ArticleEntitiesDao
import com.mr3y.ludi.shared.core.database.dao.DefaultArticleEntitiesDao
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface SharedDatabaseComponent : DatabaseDispatcherComponent {

    @Singleton
    @Provides
    fun provideDatabaseInstance(platformSqlDriverFactory: DriverFactory): LudiDatabase {
        return createDatabase(platformSqlDriverFactory)
    }

    @Singleton
    @Provides
    fun provideArticleEntitiesDaoInstance(database: LudiDatabase, dispatcher: DatabaseDispatcher): ArticleEntitiesDao {
        return DefaultArticleEntitiesDao(database, dispatcher)
    }
}
