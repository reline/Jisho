enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "database"

//pluginManagement {
//    includeBuild("../build-logic")
//    repositories {
//        gradlePluginPortal()
//        google()
//        mavenCentral()
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
//        }
//    }
//}

pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("com.github.reline.jisho")
}

dependencyResolutionManagement {
    includeBuild("../common")

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

// todo: rename to "database-plugin" or "prepopulator", etc.
rootProject.name = "database"