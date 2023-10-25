package com.mr3y.ludi.shared.core.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.mr3y.ludi.shared.ArticleEntity

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driver: DriverFactory): LudiDatabase {
    return LudiDatabase(
        driver = driver.createDriver(),
        articleEntityAdapter = ArticleEntity.Adapter(
            titleAdapter = TitleColumnAdapter,
            descriptionAdapter = MarkupTextColumnAdapter,
            sourceAdapter = EnumColumnAdapter(),
            contentAdapter = MarkupTextColumnAdapter,
            publicationDateAdapter = ZonedDateTimeAdapter
        )
    )
}
