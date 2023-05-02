package com.mr3y.ludi.core.network.interceptors

import com.mr3y.ludi.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RAWGAPIKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!chain.request().url.toString().contains("api.rawg.io")) {
            return chain.proceed(chain.request())
        }
        val request = chain.request().newBuilder()
        val updatedUrl = chain.request().url.newBuilder().addQueryParameter("key", BuildConfig.RAWG_API_KEY).build()
        request.url(updatedUrl)
        return chain.proceed(request.build())
    }
}
