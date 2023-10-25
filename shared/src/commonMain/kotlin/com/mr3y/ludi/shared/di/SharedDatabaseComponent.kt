package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.core.database.DriverFactory
import com.mr3y.ludi.shared.core.database.LudiDatabase
import com.mr3y.ludi.shared.core.database.createDatabase
import com.mr3y.ludi.shared.core.database.dao.ArticleEntitiesDao
import com.mr3y.ludi.shared.di.annotations.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

@JvmInline
value class DatabaseDispatcher(val dispatcher: CoroutineDispatcher)

interface SharedDatabaseComponent {

    @Singleton
    @Provides
    fun provideDatabaseInstance(platformSqlDriverFactory: DriverFactory): LudiDatabase {
        return createDatabase(platformSqlDriverFactory)
    }

    @Singleton
    @Provides
    fun provideIODatabaseDispatcher(): DatabaseDispatcher {
        return DatabaseDispatcher(Dispatchers.IO)
    }
}
