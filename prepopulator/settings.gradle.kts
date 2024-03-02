enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
                }
            }
            filter {
                includeGroup("com.tickaroo.tikxml")
            }
        }

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

        includeBuild("../database")
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "prepopulator"
