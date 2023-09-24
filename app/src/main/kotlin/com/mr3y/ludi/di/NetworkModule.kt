package com.mr3y.ludi.di

import android.content.Context
import com.mr3y.ludi.BuildConfig
import com.mr3y.ludi.core.network.rssparser.internal.DefaultParser
import com.prof.rssparser.Parser
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface NetworkModule {

    @Singleton
    @Provides
    fun provideThirdPartyRssParserInstance(applicationContext: Context): Parser {
        return Parser.Builder()
            .context(applicationContext)
            .cacheExpirationMillis(8L * 60L * 60L * 1000L)
            .build()
    }

    @Singleton
    @Provides
    fun provideInternalRssParserInstance(parser: Parser): com.mr3y.ludi.core.network.rssparser.Parser {
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
        return HttpClient(OkHttp) {
            install(rawgApiInterceptorPlugin)
            install(ContentNegotiation) {
                json(jsonInstance)
            }
        }
    }
}
