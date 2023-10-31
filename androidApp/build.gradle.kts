import de.fayard.refreshVersions.core.versionFor
import java.io.FileInputStream
import java.time.Instant
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.ludi.common)
    alias(libs.plugins.ludi.android.common)
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

    defaultConfig {
        applicationId = "com.mr3y.ludi"
        targetSdk = 33

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
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            matchingFallbacks += listOf("release")
            proguardFiles("benchmark-rules.pro")
            applicationIdSuffix = ".benchmark"
            // Disable uploading mapping files for the benchmark build type
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
    }

    kotlinOptions {
        jvmTarget = "17"
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

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.views.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.splash.screen)
    implementation(libs.profiler.installer)

    implementation(project(":shared"))

    // Leak Canary
    debugImplementation(libs.leakcanary)

    // Firebase expects those dependencies to be present in classpath or otherwise it crashes at runtime.
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)

    // Compose
    implementation(compose.ui)
    implementation(compose.preview)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(libs.accompanist.system.ui.controller)
    implementation(libs.voyager.navigator)

    // Tooling
    debugImplementation(compose.uiTooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.paging.testing)
    testImplementation(libs.androidx.compose.ui.test.junit4)
}
