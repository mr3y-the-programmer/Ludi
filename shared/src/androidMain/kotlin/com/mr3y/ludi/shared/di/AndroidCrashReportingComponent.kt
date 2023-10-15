package com.mr3y.ludi.shared.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.CrashlyticsReporting
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface AndroidCrashReportingComponent {
    @Singleton
    @Provides
    fun provideCrashlyticsInstance(): FirebaseCrashlytics {
        return Firebase.crashlytics
    }

    @Singleton
    @Provides
    fun provideCrashReportingInstance(crashlytics: FirebaseCrashlytics): CrashReporting {
        return CrashlyticsReporting(crashlytics)
    }
}
