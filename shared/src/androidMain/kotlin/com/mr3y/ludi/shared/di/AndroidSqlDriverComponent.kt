package com.mr3y.ludi.shared.di

import android.content.Context
import com.mr3y.ludi.shared.core.database.DriverFactory
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface AndroidSqlDriverComponent {

    @Singleton
    @Provides
    fun provideSqlDriverFactoryInstance(applicationContext: Context): DriverFactory {
        return DriverFactory(applicationContext)
    }
}
