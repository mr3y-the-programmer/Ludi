package com.mr3y.ludi.shared

import java.io.File

fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "Ludi/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/Ludi")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/Ludi")
    else -> throw IllegalStateException("Unsupported operating system")
}
