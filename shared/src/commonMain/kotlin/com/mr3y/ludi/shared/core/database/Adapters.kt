package com.mr3y.ludi.shared.core.database

import app.cash.sqldelight.ColumnAdapter
import com.mr3y.ludi.shared.core.model.MarkupText
import com.mr3y.ludi.shared.core.model.Title
import java.time.ZonedDateTime

object TitleColumnAdapter : ColumnAdapter<Title, String> {

    override fun encode(value: Title): String {
        return when (value) {
            is Title.Plain -> "Plain(${value.text})"
            is Title.Markup -> "Markup(${value.text})"
        }
    }

    override fun decode(databaseValue: String): Title {
        return when {
            databaseValue.startsWith(prefix = "Plain") -> {
                Title.Plain(databaseValue.removePrefix("Plain(").removeSuffix(")"))
            }
            databaseValue.startsWith(prefix = "Markup") -> {
                Title.Markup(databaseValue.removePrefix("Markup(").removeSuffix(")"))
            }
            else -> throw IllegalArgumentException("Unknown Title type discriminator value, Can't decode this title value: $databaseValue")
        }
    }
}

object MarkupTextColumnAdapter : ColumnAdapter<MarkupText, String> {
    override fun encode(value: MarkupText): String = value.text

    override fun decode(databaseValue: String): MarkupText = MarkupText(databaseValue)
}

object ZonedDateTimeAdapter : ColumnAdapter<ZonedDateTime, String> {
    override fun encode(value: ZonedDateTime): String = value.toString()

    override fun decode(databaseValue: String): ZonedDateTime = ZonedDateTime.parse(databaseValue)
}
