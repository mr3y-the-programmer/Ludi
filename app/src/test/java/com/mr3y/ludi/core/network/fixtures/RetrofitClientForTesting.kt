package com.mr3y.ludi.core.network.fixtures

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mr3y.ludi.core.network.interceptors.RAWGAPIKeyInterceptor
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitClientForTesting {

    private val jsonInstance = Json { ignoreUnknownKeys = true }

    private val okhttpClient = OkHttpClient.Builder().addInterceptor(RAWGAPIKeyInterceptor()).build()

    fun getInstance(baseUrl: HttpUrl): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient)
            .addConverterFactory(ApiResultConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(jsonInstance.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}