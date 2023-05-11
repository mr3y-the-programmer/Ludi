package com.mr3y.ludi.core.network.fixtures

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.TestConfig
import co.touchlab.kermit.TestLogWriter

@OptIn(ExperimentalKermitApi::class)
val TestLogger = Logger(
    config = TestConfig(
        minSeverity = Severity.Verbose,
        logWriterList = listOf(TestLogWriter(loggable = Severity.Verbose))
    ),
)