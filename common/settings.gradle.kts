enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("com.github.reline.jisho")
}

dependencyResolutionManagement {
//    repositories {
//        google()
//        mavenCentral()
//    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

// todo: rename to "database" or similar
rootProject.name = "common"
includeBuild("../ve/java")