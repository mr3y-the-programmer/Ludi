package com.mr3y.ludi.shared

import java.io.File

enum class OperatingSystem {
    Windows, Linux, MacOS, Unknown
}

val currentOperatingSystem: OperatingSystem
    get() {
        val operSys = System.getProperty("os.name").lowercase()
        return if (operSys.contains("win")) {
            OperatingSystem.Windows
        } else if (operSys.contains("nix") || operSys.contains("nux") ||
            operSys.contains("aix")
        ) {
            OperatingSystem.Linux
        } else if (operSys.contains("mac")) {
            OperatingSystem.MacOS
        } else {
            OperatingSystem.Unknown
        }
    }

fun getAppDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "LudiApp/Ludi")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".local/share/Ludi")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Application Support/Ludi")
    else -> throw IllegalStateException("Unsupported operating system")
}
