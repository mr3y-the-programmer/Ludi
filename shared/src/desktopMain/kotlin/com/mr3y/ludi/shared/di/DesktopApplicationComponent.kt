package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.Singleton
import com.mr3y.ludi.shared.getAppDir
import com.mr3y.ludi.shared.getCacheDir
import me.tatarka.inject.annotations.Component
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File

@Component
@Singleton
abstract class DesktopApplicationComponent : SharedApplicationComponent, DesktopCrashReportingComponent {

    override val dataStoreParentDir: Path = getAppDir().toOkioPath()

    override val okhttpCacheParentDir: File = getCacheDir()

    companion object
}
