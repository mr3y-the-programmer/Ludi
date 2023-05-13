package com.mr3y.ludi.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mr3y.ludi.core.network.interceptors.RAWGAPIKeyInterceptor
import com.prof.rssparser.Parser
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRssParserInstance(@ApplicationContext context: Context): Parser {
        return Parser.Builder()
            .context(context)
            .cacheExpirationMillis(8L * 60L * 60L * 1000L)
            .build()
    }

    @Provides
    @Singleton
    fun provideKotlinxSerializationJsonInstance(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(RAWGAPIKeyInterceptor()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient, jsonInstance: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.mmobomb.com/api1/")
            .client(okHttpClient)
            .addConverterFactory(ApiResultConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(jsonInstance.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
