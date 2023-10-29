import com.github.gmazzo.gradle.plugins.BuildConfigSourceSet
import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    alias(libs.plugins.ktlint.plugin)
    alias(libs.plugins.spotless.plugin)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.wire)
    alias(libs.plugins.gradle.buildconfig.plugin)
    alias(libs.plugins.sqldelight)
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
            // A shared sourceSet for (Jvm + Android) common implementations/dependencies such as Okhttp
            group("desktopAndroid") {
                withAndroidTarget()
                withJvm()
            }
        }
    }

    sourceSets {
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
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
                // Okhttp engine
                implementation(libs.ktor.okhttp)
                implementation(libs.kotlinx.serialization)

                // Sqldelight extensions
                implementation(libs.sqldelight.paging)

                // Paging
                api(libs.paging.common)
                implementation(libs.paging.compose)

                // Datastore
                implementation(libs.datastore.core)
                implementation(libs.datastore.preferences.core)
                implementation(libs.okio)

                // UI
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
                implementation(compose.foundation)
                implementation(compose.compiler.auto)
                implementation(compose.runtime)
                implementation(libs.compose.richeditor)
                // Palette
                implementation(libs.kmpalette.core)
                // Material3 WindowSizeClass
                api(libs.material3.windowsizeclass)

                // Navigation
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)

                // Molecule
                implementation(libs.molecule)

                // Annotations
                implementation(libs.androidx.annotations)

                // Resources (e.g strings)
                implementation(libs.lyricist.core)
                implementation(libs.lyricist.processor)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.strikt)
                implementation(libs.turbine)
                implementation(libs.test.parameter.injector)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.paging.testing)
            }
        }

        val desktopAndroidMain by getting {
            dependencies {

            }
        }

        val androidMain by getting {
            dependencies {
                // Database driver
                implementation(libs.sqldelight.android)
                // lifecycle
                implementation(libs.androidx.lifecycle.runtime.compose)
                // Coil
                implementation(libs.coil)
                implementation(libs.androidx.core.ktx)
                // Chrome custom tabs
                implementation(libs.androidx.browser)
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.compose.ui.test.junit4)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.test.ext.junit)
                implementation(libs.androidx.test.runner)
                implementation(libs.strikt)
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val desktopMain by getting {
            dependencies {
                // Database driver
                implementation(libs.sqldelight.jvm)
                // Crash reporting
                implementation(libs.bugsnag)

                // UI
                implementation(compose.desktop.common)
                implementation(libs.compose.imageloader)
            }
        }

        val desktopTest by getting {
            dependencies {
                dependsOn(commonTest)
            }
        }
    }
}

wire {
    kotlin {
    }
}

sqldelight {
    databases {
        create("LudiDatabase") {
            packageName.set("com.mr3y.ludi.shared.core.database")
            generateAsync.set(true)
        }
    }
}

spotless {
    format("misc") {
        // define the files to apply `misc` to
        target(listOf("**/*.gradle", "*.md", ".gitignore"))

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
    }
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.Compose.compiler)
    }

    namespace = "com.mr3y.ludi.shared"
}

buildConfig {
    packageName("com.mr3y.ludi.shared")
    sourceSets.named<BuildConfigSourceSet>("desktopMain") {
        val value = getValueOfKey("BUGSNAG_API_KEY")
        buildConfigField("String", "BUGSNAG_API_KEY", "\"$value\"")
    }
    // CommonMain
    sourceSets.named<BuildConfigSourceSet>("main") {
        val value = getValueOfKey("RAWG_API_KEY")
        buildConfigField("String", "RAWG_API_KEY", "\"$value\"")
    }
}

fun getValueOfKey(key: String) =
    if (System.getenv("CI").toBoolean()) {
        System.getenv(key)
    } else {
        val properties = Properties()
        properties.load(FileInputStream(rootProject.file("local.properties")))
        properties.getProperty(key)
    }

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.named("runKtlintFormatOverCommonMainSourceSet").configure {
    dependsOn("kspCommonMainKotlinMetadata")
}

ktlint {
    filter {
        exclude {
            it.file.path.contains("build")
        }
    }
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

dependencies {
    // Kotlin mpp plugin doesn't support adding `platform()` yet, so, we add it at top-level here.
    implementation(platform(libs.firebase.bom))
    val excludeAndroidxDataStore = Action<ExternalModuleDependency> {
        // Crashlytics depend on datastore v1.0 but we're using v1.1
        exclude(group = "androidx.datastore", module = "datastore-preferences")
    }
    // Android Crash reporting
    implementation(libs.firebase.crashlytics, excludeAndroidxDataStore)
    implementation(libs.firebase.analytics)
    // https://github.com/google/ksp/pull/1021, https://github.com/Foso/Ktorfit/blob/master/example/MultiplatformExample/shared/build.gradle.kts
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspCommonMainMetadata", libs.lyricist.processor)
    add("kspDesktop", libs.kotlin.inject.ksp)
    add("kspDesktopTest", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)
    add("kspAndroidTest", libs.kotlin.inject.ksp)
}
