package com.mr3y.ludi.shared.core

class FakeCrashReporting : CrashReporting {

    private val exceptions = mutableListOf<Throwable>()

    override fun recordException(throwable: Throwable, logMessage: String?) {
        println(logMessage)
        exceptions += throwable
    }
}
