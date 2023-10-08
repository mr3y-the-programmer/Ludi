package com.mr3y.ludi.shared.ui.screens.news

import com.mr3y.ludi.shared.core.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

actual fun durationTillNow(first: ZonedDateTime): Duration = java.time.Duration.between(
    first.toLocalDateTime(),
    ZonedDateTime.now().toLocalDateTime()
).toKotlinDuration()