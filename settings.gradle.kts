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

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

include("composeApp")
include("android")
includeBuild("database")
includeBuild("ve/java") { name = "ve" }

rootProject.name = "Jisho"
