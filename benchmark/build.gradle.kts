import com.android.build.api.dsl.ManagedVirtualDevice

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.baseline.profile)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ludi.common)
    alias(libs.plugins.ludi.android.common)
}

android {
    namespace = "com.mr3y.ludi.benchmark"

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        targetSdk = 33
    }

    buildFeatures {
        compose = false
        aidl = false
        buildConfig = false
        renderScript = false
        shaders = false
    }

    testOptions {
        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel7Api33") {
                    device = "Pixel 7"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }

    targetProjectPath = ":androidApp"
}

baselineProfile {
    managedDevices += "pixel7Api33"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.benchmark.macro.junit4)
}
