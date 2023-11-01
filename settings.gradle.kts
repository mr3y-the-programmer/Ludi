import java.net.URI

pluginManagement {
    includeBuild("convention-plugins")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        id("de.fayard.refreshVersions") version "0.60.3"
    }
}
buildscript {
    // Workaround for: https://github.com/Splitties/refreshVersions/issues/707
    dependencies {
        classpath("com.squareup.okio:okio:3.6.0")
    }
}
plugins {
    id("de.fayard.refreshVersions")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = URI.create("https://jitpack.io") }
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Ludi"
include(":androidApp")
include(":shared")
include(":desktopApp")
include(":benchmark")