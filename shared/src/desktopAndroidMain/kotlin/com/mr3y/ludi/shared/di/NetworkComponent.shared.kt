package com.mr3y.ludi.shared.di

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual fun getEngineFactory(): HttpClientEngineFactory<*> = OkHttp
