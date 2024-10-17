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
        id("de.fayard.refreshVersions") version "0.60.5"
    }
}
buildscript {
    // Workaround for: https://github.com/Splitties/refreshVersions/issues/707
    dependencies {
        classpath("com.squareup.okio:okio:3.9.0")
    }
}
plugins {
    id("de.fayard.refreshVersions")
}

refreshVersions {
    rejectVersionIf {
        // Recent versions of ktlint gradle plugin changed the default
        // code convention style which affects nearly all files in the codebase,
        // so, for now we are rejecting updates, as we are fine with the current style.
        val blacklist = listOf("org.jlleitschuh.gradle.ktlint")
        candidate.stabilityLevel.isLessStableThan(current.stabilityLevel) || moduleId.group in blacklist || candidate.value.endsWith("-jre") || candidate.value.endsWith("-1.8.20")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = URI.create("https://jitpack.io") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Ludi"
include(":androidApp")
include(":shared")
include(":desktopApp")