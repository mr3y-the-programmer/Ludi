package com.mr3y.ludi.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "com.mr3y.ludi",
        stableIterations = 2,
        maxIterations = 10,
        profileBlock = {
            pressHome()
            startActivityAndWait()

            // simulate user journey through the app.
        }
    )
}