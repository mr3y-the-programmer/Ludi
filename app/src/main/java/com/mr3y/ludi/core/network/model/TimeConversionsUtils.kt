package com.mr3y.ludi.core.network.model

import java.text.ParsePosition
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

internal fun Long.convertEpochSecondToZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
}

/**
 * Converts the following date format: "yyyy-mm-dd" to a well-structured ZonedDateTime
 */
internal fun String.toZonedDate(): ZonedDateTime {
    val (year, month, dayOfMonth) = split('-').map { it.toInt() }
    return ZonedDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, ZoneId.systemDefault())
}

internal fun String.toZonedDateTime(pattern: Pattern = Pattern.RFC_1123): ZonedDateTime? {
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

internal enum class Pattern {
    /**
     * A date time format pattern for date time strings like: "Thu, 02 Mar 2023 03:00:00 -0800", "Thu, 2 Mar 2023 01:30:02 +0000"
     */
    RFC_1123,

    /**
     * A date time format pattern for date time strings like: "2023-03-24T15:16:31Z", "2023-03-24 15:16:31"
     */
    ISO_UTC_DATE_TIME
}
