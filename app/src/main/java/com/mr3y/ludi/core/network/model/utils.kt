package com.mr3y.ludi.core.network.model

import java.time.ZoneId
import java.time.ZonedDateTime

internal fun String?.ignoreIfEmptyOrNull(): String? {
    return takeIf { this != null && this.isNotEmpty() }
}

/**
 * Converts the following date format: "yyyy-mm-dd" to a well-structured ZonedDateTime
 */
internal fun String.toZonedDate(): ZonedDateTime {
    val (year, month, dayOfMonth) = split('-').map { it.toInt() }
    return ZonedDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, ZoneId.systemDefault())
}

/**
 * Converts the following date time format: "Thu, 02 Mar 2023 03:00:00 -0800", "Thu, 2 Mar 2023 01:30:02 +0000"
 * to a valid ZonedDateTime.
 */
internal fun String.toZonedDateTime(): ZonedDateTime {
    val (dayOfMonth, monthName, year, time, zoneOffset) = split(" ").drop(1)
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
