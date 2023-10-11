package com.mr3y.ludi.shared.core.network.fixtures

import com.mr3y.ludi.shared.core.Logger

class TestLogger : Logger {

    private val _logs = mutableListOf<String>()
    val logs: List<String>
        get() = _logs

    override fun d(throwable: Throwable?, tag: String, message: () -> String) {
        val evaluatedMessage = message()
        _logs += evaluatedMessage
        println("$tag: $evaluatedMessage")
    }

    override fun i(throwable: Throwable?, tag: String, message: () -> String) {
        val evaluatedMessage = message()
        _logs += evaluatedMessage
        println("$tag: $evaluatedMessage")
    }

    override fun e(throwable: Throwable?, tag: String, message: () -> String) {
        val evaluatedMessage = message()
        _logs += evaluatedMessage
        println("$tag: $evaluatedMessage - $throwable")
    }

    override fun v(throwable: Throwable?, tag: String, message: () -> String) {
        val evaluatedMessage = message()
        _logs += evaluatedMessage
        println("$tag: $evaluatedMessage")
    }

    override fun w(throwable: Throwable?, tag: String, message: () -> String) {
        val evaluatedMessage = message()
        _logs += evaluatedMessage
        println("$tag: $evaluatedMessage - $throwable")
    }

    fun reset() {
        _logs.clear()
    }
}
