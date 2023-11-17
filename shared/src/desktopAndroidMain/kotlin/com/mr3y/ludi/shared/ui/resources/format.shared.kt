// ktlint-disable filename
package com.mr3y.ludi.shared.ui.resources

import java.math.RoundingMode

actual fun Float.roundToTwoDecimalDigits(): Float = toBigDecimal().setScale(2, RoundingMode.HALF_UP).toFloat()
