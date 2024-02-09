enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// todo: clean the plugin block up
pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
//    includeBuild("app/android") // fixme: for multiplatform
    includeBuild("android")
    includeBuild("database")
}

rootProject.name = "Jisho"
