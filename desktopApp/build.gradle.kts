import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.ktlint.plugin)
    alias(libs.plugins.spotless.plugin)
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.common)
                implementation(project(":shared"))
            }
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

dependencies {
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

compose.desktop {
    sourceSets["main"].resources.srcDirs("src/jvmMain/resources")
    application {
        mainClass = "MainKt"

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
