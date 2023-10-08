package com.mr3y.ludi.shared.ui.presenter

import com.mr3y.ludi.shared.core.time.ZoneId
import com.mr3y.ludi.shared.core.time.ZonedDateTime

actual fun ZonedDateTime.isInFuture(): Boolean = this.isAfter(ZonedDateTime.now(ZoneId.systemDefault()))
