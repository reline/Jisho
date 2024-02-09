pluginManagement {
    includeBuild("../build-logic")
    includeBuild("../database")
    // fixme: use a convention plugin, see below plugins block
//    repositories {
//        gradlePluginPortal()
//        google()
//        mavenCentral()
//    }
}

plugins {
    id("com.github.reline.jisho")
    /**
     * // settings.gradle.kts
     *
     * dependencyResolutionManagement {
     *     versionCatalogs {
     *         create("libs") {
     *             from(files("../gradle/libs.versions.toml"))
     *         }
     *     }
     * }
     *
     * // build.gradle.kts
     *
     * dependencies {
     *     implementation(libs.plugins.android.application)
     * }
     *
     * // com.github.reline.jisho.build.gradle.kts
     *
     * plugins {
     *     id("com.android.application")
     * }
     */
}

dependencyResolutionManagement {
    includeBuild("../common")

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "android"