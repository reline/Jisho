fun RepositoryHandler.jishoRepositories() {
    google()
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
}

pluginManagement.repositories {
    gradlePluginPortal()
    jishoRepositories()
}

dependencyResolutionManagement.repositories {
//    jishoRepositories()

    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://www.jitpack.io")
            }
        }
        filter {
            includeGroup("com.github.reline")
        }
    }
}
