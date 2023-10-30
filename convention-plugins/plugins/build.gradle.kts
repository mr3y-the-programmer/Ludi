import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.mr3y.ludi.conventionplugins"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.ktlint.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("commonConventionPlugin") {
            id = "ludi.common"
            implementationClass = "com.mr3y.ludi.gradle.CommonConventionPlugin"
        }
        register("androidConventionPlugin") {
            id = "ludi.android.common"
            implementationClass = "com.mr3y.ludi.gradle.AndroidConventionPlugin"
        }
    }
}
