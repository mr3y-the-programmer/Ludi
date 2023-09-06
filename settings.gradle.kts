import java.net.URI

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("de.fayard.refreshVersions") version "0.60.2"
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
    }
}
rootProject.name = "Ludi"
include(":app")
