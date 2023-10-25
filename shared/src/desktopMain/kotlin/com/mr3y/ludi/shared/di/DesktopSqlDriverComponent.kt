package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.core.database.DriverFactory
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface DesktopSqlDriverComponent {

    @Singleton
    @Provides
    fun provideJdbcSqlDriverFactoryInstance(): DriverFactory {
        return DriverFactory()
    }
}