package com.mr3y.ludi.shared.core.network.fixtures

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

object KtorClientForTesting {

    private val jsonInstance = Json { ignoreUnknownKeys = true }

    fun getInstance(dispatcher: CoroutineDispatcher): HttpClient {
        val engine = MockEngine.create {
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

fun HttpClient.enqueueMockResponse(response: String, status: HttpStatusCode, headers: Set<Pair<String, String>> = emptySet()) {
    (this.engineConfig as? MockEngineConfig)?.apply {
        requestHandlers.clear()
        addHandler { _ ->
            respond(
                content = response,
                status = status,
                headers = headers {
                    append(HttpHeaders.ContentType, "application/json")
                    headers.forEach { (name, value) ->
                        append(name, value)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalContracts::class)
fun HttpClient.interceptOutgoingRequest(action: (HttpRequestData) -> Unit) {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    (this.engineConfig as? MockEngineConfig)?.apply {
        requestHandlers.clear()
        addHandler { requestData ->
            action(requestData)
            respondOk()
        }
    }
}

fun HttpClient.doCleanup() {
    (this.engineConfig as? MockEngineConfig)?.requestHandlers?.clear()
}
