package com.mr3y.ludi.core.internal

import com.mr3y.ludi.core.Logger
import javax.inject.Inject
import co.touchlab.kermit.Logger as DelegatingLogger

class KermitLogger @Inject constructor(
    private val delegatingLogger: DelegatingLogger
) : Logger {

    override fun d(throwable: Throwable?, tag: String, message: () -> String) {
        delegatingLogger.d(throwable, tag, message)
    }

    override fun i(throwable: Throwable?, tag: String, message: () -> String) {
        delegatingLogger.i(throwable, tag, message)
    }

    override fun e(throwable: Throwable?, tag: String, message: () -> String) {
        delegatingLogger.e(throwable, tag, message)
    }

    override fun v(throwable: Throwable?, tag: String, message: () -> String) {
        delegatingLogger.v(throwable, tag, message)
    }

    override fun w(throwable: Throwable?, tag: String, message: () -> String) {
        delegatingLogger.w(throwable, tag, message)
    }
}
