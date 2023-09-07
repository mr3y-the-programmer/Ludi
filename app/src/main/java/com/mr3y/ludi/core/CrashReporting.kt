package com.mr3y.ludi.core

interface CrashReporting {

    fun recordException(throwable: Throwable, logMessage: String?)
}
