package com.mr3y.ludi.shared.ui.components

import com.mr3y.ludi.shared.OperatingSystem
import com.mr3y.ludi.shared.currentOperatingSystem
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import okio.Path.Companion.toOkioPath
import java.io.File

internal fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toOkioPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

private fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "Ludi/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/Ludi")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/Ludi")
    else -> throw IllegalStateException("Unsupported operating system")
}
