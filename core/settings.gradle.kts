enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("../build-logic")

    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("com.github.reline.jisho.gradle")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include("unicode")
include("database")
include("database-readonly")
includeBuild("../ve/java") { name = "ve" }

rootProject.name = "core"
