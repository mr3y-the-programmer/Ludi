package com.mr3y.ludi.shared.ui.screens.deals

import java.time.Duration
import java.time.ZonedDateTime
import kotlin.time.toKotlinDuration

actual fun durationBetween(start: ZonedDateTime, end: ZonedDateTime): Triple<String, String, String> {
    val duration = Duration.between(start, end).toKotlinDuration().toString()
    val days = duration.substringBefore('d')
    val hours = duration.substringAfter('d').substringBefore('h')
    val minutes = duration.substringAfter('h').substringBefore('m')
    return Triple(days, hours, minutes)
}