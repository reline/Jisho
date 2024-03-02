enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://www.atilika.org/nexus/content/repositories/atilika")
                }
            }
            filter {
                includeGroup("org.atilika.kuromoji")
            }
        }

        includeBuild("../ve/java") {
            name = "ve"
        }
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "database"
