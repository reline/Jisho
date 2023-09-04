pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        maven {
            url = uri("https://repo.maven.apache.org/maven2")
        }
        maven {
            url = uri("https://www.jitpack.io")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://www.atilika.org/nexus/content/repositories/atilika")
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven {
            url = uri("https://repo.maven.apache.org/maven2")
        }
        maven {
            url = uri("https://www.jitpack.io")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://www.atilika.org/nexus/content/repositories/atilika")
        }
    }
}

rootProject.name = "database"

include(":common", ":ve")
