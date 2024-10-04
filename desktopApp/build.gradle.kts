import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ludi.common)
}

kotlin {
    jvm {
        withJava()
    }
    jvmToolchain(17)
    sourceSets {
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.common)
                implementation(project(":shared"))
            }
        }
    }
}

dependencies {
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

compose.desktop {
    sourceSets["main"].resources.srcDirs("src/jvmMain/resources")
    application {
        mainClass = "com.mr3y.ludi.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.mr3y.ludi.desktop"

            description = "Ludi is a Kotlin Multiplatform (Android + Desktop) app to demonstrate best practices & using modern technologies to develop high quality apps"
            copyright = "Apache License Version 2.0"
            vendor = "mr3y-the-programmer"

            buildTypes.release.proguard {
                isEnabled.set(true)
                configurationFiles.from("proguard-rules.pro")
            }
            windows {
                shortcut = true
                menu = true
                menuGroup = "Ludi"
                iconFile.set(file("src/jvmMain/resources/icon_light.ico"))
            }
            macOS {
                bundleID = "com.mr3y.ludi"
                packageName = "com.mr3y.ludi.desktop"
                // TODO: provide .icns icon
                iconFile.set(file("src/jvmMain/resources/icon_light.xml"))
            }
            linux {
                iconFile.set(file("src/jvmMain/resources/icon_light.png"))
            }
        }
    }
}
