package com.mr3y.ludi.shared.di

import android.os.SystemClock
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides
import kotlin.time.AbstractLongTimeSource
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

actual interface TimeSourceComponent {

    @Provides
    @Singleton
    fun provideTimeSourceInstance(): TimeSource {
        return object : AbstractLongTimeSource(DurationUnit.NANOSECONDS) {
            override fun read(): Long = SystemClock.elapsedRealtimeNanos()

            override fun toString(): String = "TimeSource(SystemClock.elapsedRealtimeNanos())"
        }
    }
}
