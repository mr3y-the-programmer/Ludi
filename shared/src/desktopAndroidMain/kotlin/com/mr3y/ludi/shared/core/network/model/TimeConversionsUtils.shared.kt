package com.mr3y.ludi.shared.core.network.model

import com.mr3y.ludi.shared.core.time.DateTimeFormatter
import com.mr3y.ludi.shared.core.time.Instant
import com.mr3y.ludi.shared.core.time.ZoneId
import com.mr3y.ludi.shared.core.time.ZonedDateTime
import java.text.ParsePosition
import java.time.format.DateTimeParseException

internal actual fun Long.convertEpochSecondToZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
}

internal actual fun String.toZonedDate(): ZonedDateTime {
    val (year, month, dayOfMonth) = split('-').map { it.toInt() }
    return ZonedDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, ZoneId.systemDefault())
}

internal actual fun String.toZonedDateTime(pattern: Pattern): ZonedDateTime? {
    return if (DateTimeFormatter.ISO_ZONED_DATE_TIME.parseUnresolved(this, ParsePosition(0)) != null) {
        ZonedDateTime.parse(this)
    } else {
        try {
            when (pattern) {
                Pattern.RFC_1123 -> ZonedDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME)
                Pattern.ISO_UTC_DATE_TIME -> ZonedDateTime.parse("${this.replace(' ', 'T').removeSuffix("Z")}Z")
            }
        } catch (e: DateTimeParseException) {
            null
        }
    }
}
