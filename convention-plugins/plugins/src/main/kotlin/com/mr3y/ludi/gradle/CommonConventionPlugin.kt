package com.mr3y.ludi.gradle

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class CommonConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jlleitschuh.gradle.ktlint")
            pluginManager.apply("com.diffplug.spotless")

            val ktlintExtension = extensions.getByType<KtlintExtension>()
            val spotlessExtension = extensions.getByType<SpotlessExtension>()
            configureKtlintExtension(ktlintExtension)
            configureSpotlessExtension(spotlessExtension)
        }
    }

    private fun Project.configureKtlintExtension(extension: KtlintExtension) {
        extension.apply {
            filter {
                exclude {
                    it.file.path.contains("build")
                }
            }
        }
    }

    private fun Project.configureSpotlessExtension(extension: SpotlessExtension) {
        extension.apply {
            format("misc") {
                // define the files to apply `misc` to
                target(listOf("**/*.gradle", "*.md", ".gitignore", "**/*.gradle.kts"))

                // define the steps to apply to those files
                trimTrailingWhitespace()
                indentWithSpaces(4)
                endWithNewline()
            }
        }
    }
}
