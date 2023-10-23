package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.BuildConfig
import com.mr3y.ludi.shared.core.Logger
import com.mr3y.ludi.shared.core.network.rssparser.Parser
import com.mr3y.ludi.shared.core.network.rssparser.internal.DefaultParser
import com.mr3y.ludi.shared.di.annotations.Singleton
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.RssParserBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import okhttp3.Cache
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.File
import java.io.IOException

interface NetworkComponent {

    @get:Provides
    val okhttpCacheParentDir: File

    @Singleton
    @Provides
    fun provideOkhttpClientInstance(okhttpCacheParentDir: File, logger: Logger): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(directory = File(okhttpCacheParentDir, "okhttp_cache"), maxSize = 80L * 1024L * 1024L))
            .eventListener(object : EventListener() {
                override fun callStart(call: Call) {
                    logger.v { "Started Executing call: $call" }
                }

                override fun cacheHit(call: Call, response: Response) {
                    logger.d { "Cache Hit for this request: $call! Retrieving response from cache, cached response is : $response." }
                }

                override fun cacheMiss(call: Call) {
                    logger.d { "Cache Miss for this request: $call! Retrieving response from network" }
                }

                override fun cacheConditionalHit(call: Call, cachedResponse: Response) {
                    logger.d { "Checking if cached $cachedResponse isn't stale for request: $call." }
                }

                override fun callFailed(call: Call, ioe: IOException) {
                    logger.w { "Failed to execute call: $call, exception: $ioe" }
                }
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideThirdPartyRssParserInstance(okhttpClient: OkHttpClient): RssParser {
        return RssParserBuilder(callFactory = okhttpClient).build()
    }

    @Singleton
    @Provides
    fun provideInternalRssParserInstance(parser: RssParser): Parser {
        return DefaultParser(parser)
    }

    @Provides
    @Singleton
    fun provideKotlinxSerializationJsonInstance(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    @Singleton
    fun provideKtorClientInstance(okhttpClient: OkHttpClient, jsonInstance: Json): HttpClient {
        val rawgApiInterceptorPlugin = createClientPlugin("RAWGAPIKeyInterceptor") {
            onRequest { request, _ ->
                if (request.url.toString().contains("api.rawg.io")) {
                    request.url {
                        parameters.append("key", BuildConfig.RAWG_API_KEY)
                    }
                }
            }
        }
        return HttpClient(OkHttp) {
            engine {
                preconfigured = okhttpClient
            }
            install(HttpRequestRetry) {
                retryIf(3) { _, httpResponse ->
                    when {
                        httpResponse.status.value in 500..599 -> true
                        httpResponse.status == HttpStatusCode.TooManyRequests -> true
                        else -> false
                    }
                }
                constantDelay()
            }
            install(rawgApiInterceptorPlugin)
            install(ContentNegotiation) {
                json(jsonInstance)
            }
        }
    }
}
