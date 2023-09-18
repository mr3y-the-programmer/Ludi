import com.diffplug.spotless.LineEnding
import de.fayard.refreshVersions.core.versionFor
import java.io.FileInputStream
import java.time.Instant
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.gradle.buildconfig.plugin)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ktlint.plugin)
    alias(libs.plugins.spotless.plugin)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.gradle.play.publisher)
    alias(libs.plugins.app.versioning)
}

android {
    signingConfigs {
        create("release") {
            if (rootProject.file("keystore.properties").exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(rootProject.file("keystore.properties")))
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
            }
        }
    }
    namespace = "com.mr3y.ludi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mr3y.ludi"
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "com.mr3y.ludi.runner.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            // Exclude files that unused kotlin-reflect would need, to make the app smaller:
            // (see issue https://youtrack.jetbrains.com/issue/KT-9770)
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/*.kotlin_module",
                "kotlin/*.kotlin_builtins",
                "kotlin/**/*.kotlin_builtins",
                "META-INF/LICENSE*",
                "META-INF/NOTICE.txt"
            )
        }
    }
}

play {
    if (rootProject.file("play_config.json").exists()) {
        serviceAccountCredentials.set(rootProject.file("play_config.json"))
        defaultToAppBundles.set(true)
        track.set("beta")
    } else {
        enabled.set(false)
    }
}

appVersioning {
    overrideVersionCode { _, _, _ ->
        Instant.now().epochSecond.toInt()
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
    // Workaround Config cache problem until https://github.com/diffplug/spotless/issues/1644 is fixed.
    lineEndings = LineEnding.PLATFORM_NATIVE // or any other except GIT_ATTRIBUTES
}

buildConfig {
    packageName("com.mr3y.ludi")
    val key = if (System.getenv("CI").toBoolean()) {
        System.getenv("RAWG_API_KEY")
    } else {
        val properties = Properties()
        properties.load(FileInputStream(rootProject.file("local.properties")))
        properties.getProperty("RAWG_API_KEY")
    }
    buildConfigField("String", "RAWG_API_KEY", "\"$key\"")
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
                val kotlin by registering {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.splash.screen)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt and instrumented tests.
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    // Hilt and Robolectric tests.
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    // Logging
    implementation(libs.kermit)

    // Crashlytics
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Leak Canary
    debugImplementation(libs.leakcanary)

    // Datastore
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    implementation(libs.protobuf.kotlinlite)

    // Network
    implementation(libs.rss.parser)
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.content.negotation)
    implementation(libs.ktor.kotlinx.serialization)
    implementation(libs.kotlinx.serialization)

    // Arch Components
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil)
    implementation(libs.accompanist.system.ui.controller)
    implementation(libs.fornewid.placeholder)
    implementation(libs.compose.html)
    // Palette
    implementation(libs.androidx.palette)
    // Chrome custom tabs
    implementation(libs.androidx.browser)
    // Caching In-memory data
    implementation(libs.cache4k)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Instrumented tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.strikt)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.test.parameter.injector)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.mock)

    // Instrumented tests: jUnit rules and runners

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.strikt)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
