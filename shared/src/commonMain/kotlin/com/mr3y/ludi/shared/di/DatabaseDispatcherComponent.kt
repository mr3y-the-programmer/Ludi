package com.mr3y.ludi.shared.di

import kotlinx.coroutines.CoroutineDispatcher

expect interface DatabaseDispatcherComponent

@JvmInline
value class DatabaseDispatcher(val dispatcher: CoroutineDispatcher)
