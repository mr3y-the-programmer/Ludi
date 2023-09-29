plugins {
    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.compose.multiplatform)
    application
    id("org.jetbrains.compose")
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

application {
    mainClass.set("MainKt")
}
