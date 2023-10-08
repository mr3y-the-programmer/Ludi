package com.mr3y.ludi.shared.ui.screens.news

import com.mr3y.ludi.shared.core.time.ZonedDateTime
import java.time.LocalDate

actual fun ZonedDateTime.toLocalDate(): LocalDate = this.toLocalDate()