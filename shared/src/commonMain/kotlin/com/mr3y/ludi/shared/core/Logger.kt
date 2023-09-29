package com.mr3y.ludi.shared.core

interface Logger {

    fun d(throwable: Throwable? = null, tag: String = "", message: () -> String)

    fun i(throwable: Throwable? = null, tag: String = "", message: () -> String)

    fun e(throwable: Throwable? = null, tag: String = "", message: () -> String)

    fun v(throwable: Throwable? = null, tag: String = "", message: () -> String)

    fun w(throwable: Throwable? = null, tag: String = "", message: () -> String)
}
