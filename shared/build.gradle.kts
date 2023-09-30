import com.github.gmazzo.gradle.plugins.BuildConfigSourceSet
import java.io.FileInputStream
import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.gradle.buildconfig.plugin)
}

kotlin {
    androidTarget {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm("desktop") {
        jvmToolchain(17)
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.custom {
        common {
            // A shared sourceSet for (Jvm + Android) common implementations/dependencies such as Okhttp, java.time API
            group("desktopAndroid") {
                withAndroidTarget()
                withJvm()
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Logging
                implementation(libs.kermit)

                // Dependency Injection
                implementation(libs.kotlin.inject.runtime)

                // Network
                implementation(libs.rss.parser)
                implementation(libs.ktor.core)
                implementation(libs.ktor.content.negotation)
                implementation(libs.ktor.kotlinx.serialization)
                implementation(libs.kotlinx.serialization)

                // UI
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.foundation)
                implementation(compose.compiler.auto)
                implementation(compose.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopAndroidMain by getting {
            dependencies {
                // Okhttp engine
                implementation(libs.ktor.okhttp)
            }
        }

        val androidMain by getting {
            dependencies {
                // Crash reporting
                implementation(libs.firebase.crashlytics)
                implementation(libs.firebase.analytics)
            }
        }

        val desktopMain by getting {
            dependencies {
                // Crash reporting
                implementation(libs.bugsnag)

                // UI
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    namespace = "com.mr3y.ludi.shared"
}

buildConfig {
    packageName("com.mr3y.ludi.shared")
    sourceSets.named<BuildConfigSourceSet>("desktopMain") {
        val key = if (System.getenv("CI").toBoolean()) {
            System.getenv("BUGSNAG_API_KEY")
        } else {
            val properties = Properties()
            properties.load(FileInputStream(rootProject.file("local.properties")))
            properties.getProperty("BUGSNAG_API_KEY")
        }
        buildConfigField("String", "BUGSNAG_API_KEY", "\"$key\"")
    }
}

dependencies {
    // Kotlin mpp plugin doesn't support adding `platform()` yet, so, we add it at top-level here.
    implementation(platform(libs.firebase.bom))
    // https://github.com/google/ksp/pull/1021, https://github.com/Foso/Ktorfit/blob/master/example/MultiplatformExample/shared/build.gradle.kts
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspDesktop", libs.kotlin.inject.ksp)
    add("kspDesktopTest", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)
    add("kspAndroidTest", libs.kotlin.inject.ksp)
}
