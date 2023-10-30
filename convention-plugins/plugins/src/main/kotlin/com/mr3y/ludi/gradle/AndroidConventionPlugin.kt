package com.mr3y.ludi.gradle

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin("com.android.library") -> {
                    val androidLibExtension = extensions.getByType<LibraryExtension>()
                    androidLibExtension.apply {
                        applyCommonAndroidConvention()
                    }
                }
                pluginManager.hasPlugin("com.android.application") -> {
                    val androidAppExtension = extensions.getByType<BaseAppModuleExtension>()
                    androidAppExtension.apply {
                        applyCommonAndroidConvention()
                    }
                }
                else -> {}
            }
        }
    }

    private fun CommonExtension<*, *, *, *, *>.applyCommonAndroidConvention() {
        compileSdk = 34

        defaultConfig {
            minSdk = 26

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        buildFeatures {
            compose = true
            aidl = false
            buildConfig = false
            renderScript = false
            shaders = false
        }
    }
}
