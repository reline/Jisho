pluginManagement {
    includeBuild("build-logic")
    includeBuild("prepopulator")

    repositories {
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
    }
}

plugins {
    id("com.github.reline.jisho.gradle")
}

includeBuild("android")
includeBuild("core")

rootProject.name = "Jisho"
