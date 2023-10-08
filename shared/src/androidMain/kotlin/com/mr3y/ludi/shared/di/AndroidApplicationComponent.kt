package com.mr3y.ludi.shared.di

import android.content.Context
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okio.Path
import okio.Path.Companion.toOkioPath

@Component
@Singleton
abstract class AndroidApplicationComponent(
    @get:Provides val applicationContext: Context
) : SharedApplicationComponent, AndroidCrashReportingComponent {

    override val dataStoreParentDir: Path = applicationContext.filesDir.toOkioPath()

    companion object
}