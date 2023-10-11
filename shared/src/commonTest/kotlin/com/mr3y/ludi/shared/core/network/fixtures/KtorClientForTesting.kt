package com.mr3y.ludi.shared.core.network.fixtures

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

object KtorClientForTesting {

    private val jsonInstance = Json { ignoreUnknownKeys = true }

    fun getInstance(dispatcher: CoroutineDispatcher): HttpClient {
        val engine = MockEngine.create {
            this.dispatcher = dispatcher
            requestHandlers.add {
                respondOk()
            }
        }
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(jsonInstance)
            }
        }
    }
}

fun HttpClient.enqueueMockResponse(response: String, status: HttpStatusCode) {
    (this.engineConfig as? MockEngineConfig)?.apply {
        requestHandlers.clear()
        addHandler { _ ->
            respond(
                content = response,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, listOf("application/json"))
            )
        }
    }
}

fun HttpClient.doCleanup() {
    (this.engineConfig as? MockEngineConfig)?.requestHandlers?.clear()
}
