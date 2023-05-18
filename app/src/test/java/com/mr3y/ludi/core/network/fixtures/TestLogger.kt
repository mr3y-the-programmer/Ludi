package com.mr3y.ludi.core.network.fixtures

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.TestConfig
import co.touchlab.kermit.TestLogWriter

@OptIn(ExperimentalKermitApi::class)
fun logger(severity: Severity = Severity.Verbose, logWriter: LogWriter? = null): Logger {
    return Logger(
        config = TestConfig(
            minSeverity = severity,
            logWriterList = listOf(logWriter ?: TestLogWriter(loggable = severity))
        )
    )
}

@OptIn(ExperimentalKermitApi::class)
val TestLogger = Logger(
    config = TestConfig(
        minSeverity = Severity.Verbose,
        logWriterList = listOf(PrintlnLogWriter())
    ),
)

class PrintlnLogWriter : LogWriter() {

    private val _logs = mutableListOf<String>()
    val logs: List<String>
        get() = _logs

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        _logs += message
        if (throwable != null) println("$message $throwable") else println(message)
    }

    fun reset() {
        _logs.clear()
    }
}