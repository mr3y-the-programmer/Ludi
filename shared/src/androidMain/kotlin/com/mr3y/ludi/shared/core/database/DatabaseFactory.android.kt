// ktlint-disable filename
package com.mr3y.ludi.shared.core.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import me.tatarka.inject.annotations.Inject

@Inject
actual class DriverFactory(private val applicationContext: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = LudiDatabase.Schema.synchronous(),
            context = applicationContext,
            name = "ludi_database.db",
            factory = RequerySQLiteOpenHelperFactory()
        )
    }
}
