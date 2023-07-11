package com.mr3y.ludi.core.network.model

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

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

internal fun String.toZonedDateTime(pattern: Pattern = Pattern.RFC_1123, isLenient: Boolean = false): ZonedDateTime {
    return when (pattern) {
        Pattern.RFC_1123 -> fromRFC1123ToZonedDateTime(this)
        Pattern.ISO_UTC_DATE_TIME -> fromISOUTCToZonedDateTime(this, isLenient)
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

private fun fromRFC1123ToZonedDateTime(from: String): ZonedDateTime {
    val (dayOfMonth, monthName, year, time, zoneOffset) = from.split(" ").drop(1)
    val (hour, minute, second) = time.split(":").map { it.toInt() }
    return ZonedDateTime.of(
        year.toInt(),
        monthName.toGregorianMonthNumber(),
        dayOfMonth.toInt(),
        hour,
        minute,
        second,
        0,
        ZoneId.of(zoneOffset)
    )
}

private fun fromISOUTCToZonedDateTime(from: String, isLenient: Boolean): ZonedDateTime {
    val delimiters = if (isLenient) charArrayOf(' ', 'T') else charArrayOf('T')
    val (date, time) = from.split(delimiters = delimiters)
    val (year, month, dayOfMonth) = date.split('-').map { it.toInt() }
    val (hour, minute, second) = time.split(':').map { it.toInt() }
    return ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, 0, ZoneId.systemDefault())
}

private fun String.toGregorianMonthNumber(): Int {
    return when {
        this.contains("JAN", ignoreCase = true) -> 1
        this.contains("FEB", ignoreCase = true) -> 2
        this.contains("MAR", ignoreCase = true) -> 3
        this.contains("APR", ignoreCase = true) -> 4
        this.contains("MAY", ignoreCase = true) -> 5
        this.contains("JUN", ignoreCase = true) -> 6
        this.contains("JUL", ignoreCase = true) -> 7
        this.contains("AUG", ignoreCase = true) -> 8
        this.contains("SEP", ignoreCase = true) -> 9
        this.contains("OCT", ignoreCase = true) -> 10
        this.contains("NOV", ignoreCase = true) -> 11
        this.contains("DEC", ignoreCase = true) -> 12
        else -> throw IllegalArgumentException("Invalid month name")
    }
}
