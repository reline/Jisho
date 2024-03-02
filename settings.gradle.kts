enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

includeBuild("database")
includeBuild("prepopulator")
includeBuild("android")

rootProject.name = "Jisho"
