package com.mr3y.ludi.shared.di

import com.bugsnag.Bugsnag
import com.mr3y.ludi.shared.DesktopMainBuildConfig
import com.mr3y.ludi.shared.core.BugsnagReporting
import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface DesktopCrashReportingComponent {

    @Singleton
    @Provides
    fun provideBugsnagClientInstance(): Bugsnag {
        return Bugsnag(DesktopMainBuildConfig.BUGSNAG_API_KEY)
    }

    @Singleton
    @Provides
    fun provideCrashReportingInstance(bugsnagClient: Bugsnag): CrashReporting {
        return BugsnagReporting(bugsnagClient)
    }
}