package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides
import kotlin.time.TimeSource

actual interface TimeSourceComponent {
    @Provides
    @Singleton
    fun provideTimeSourceInstance(): TimeSource = TimeSource.Monotonic
}
