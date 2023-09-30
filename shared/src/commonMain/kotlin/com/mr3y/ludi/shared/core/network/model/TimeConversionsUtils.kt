package com.mr3y.ludi.shared.core.network.model

import com.mr3y.ludi.shared.core.time.ZonedDateTime

internal expect fun Long.convertEpochSecondToZonedDateTime(): ZonedDateTime

/**
 * Converts the following date format: "yyyy-mm-dd" to ZonedDateTime
 */
internal expect fun String.toZonedDate(): ZonedDateTime

internal expect fun String.toZonedDateTime(pattern: Pattern = Pattern.RFC_1123): ZonedDateTime?

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
