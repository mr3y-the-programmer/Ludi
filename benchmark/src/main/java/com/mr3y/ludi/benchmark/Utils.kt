package com.mr3y.ludi.benchmark

import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

internal fun UiDevice.waitForObject(selector: BySelector, timeout: Long = 1000L): UiObject2 {
    if (wait(Until.hasObject(selector), timeout)) {
        return findObject(selector)
    }
    error("Object with selector [$selector] not found")
}
