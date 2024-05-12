pluginManagement {
    includeBuild("build-logic")
    includeBuild("prepopulator")
}

plugins {
    id("com.github.reline.jisho.gradle")
}

include("android")
includeBuild("database")
includeBuild("ve/java") { name = "ve" }

rootProject.name = "Jisho"
