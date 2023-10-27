// ktlint-disable filename
package com.mr3y.ludi.shared.core.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mr3y.ludi.shared.getAppDir
import me.tatarka.inject.annotations.Inject
import java.io.File

@Inject
actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val databaseFile = File(getAppDir(), "ludi_database.db")
        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${databaseFile.path}")
        LudiDatabase.Schema.synchronous().create(driver)
        return driver
    }
}
