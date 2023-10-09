package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.BuildConfig
import com.mr3y.ludi.shared.core.network.rssparser.Parser
import com.mr3y.ludi.shared.core.network.rssparser.internal.DefaultParser
import com.mr3y.ludi.shared.di.annotations.Singleton
import com.prof18.rssparser.RssParser
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface NetworkComponent {

    @Singleton
    @Provides
    fun provideThirdPartyRssParserInstance(): RssParser {
        return RssParser()
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
    fun provideKtorClientInstance(jsonInstance: Json): HttpClient {
        val rawgApiInterceptorPlugin = createClientPlugin("RAWGAPIKeyInterceptor") {
            onRequest { request, _ ->
                if (request.url.toString().contains("api.rawg.io")) {
                    request.url {
                        parameters.append("key", BuildConfig.RAWG_API_KEY)
                    }
                }
            }
        }
        return HttpClient(getEngineFactory()) {
            install(rawgApiInterceptorPlugin)
            install(ContentNegotiation) {
                json(jsonInstance)
            }
        }
    }
}

expect fun getEngineFactory(): HttpClientEngineFactory<*>