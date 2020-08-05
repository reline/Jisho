plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(16)
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile(File("src/androidMain/AndroidManifest.xml"))
        }
    }
}

sqldelight {
    database("JishoDatabase") {
        packageName = "com.github.reline.jisho.sql"
        schemaOutputDirectory = file("src/commonMain/sqldelight/com/github/reline/jisho/sql/migrations")
    }
}

kotlin {
    targets {
        android()
        jvm()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.coroutines}")

                // SQL Delight
                implementation("com.squareup.sqldelight:runtime:${Versions.sqldelight}")
                implementation("com.squareup.sqldelight:coroutines-extensions:${Versions.sqldelight}")
            }
        }

        val jvmMain by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

                // SQL Delight
                implementation("com.squareup.sqldelight:sqlite-driver:${Versions.sqldelight}")

                // Ve
                implementation(project(":ve"))
            }
        }

        val androidMain by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

                // SQL Delight
                implementation("com.squareup.sqldelight:android-driver:${Versions.sqldelight}")
                implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${Versions.sqldelight}")

                // Ve
                implementation(project(":ve"))
            }
        }

        val jvmTest by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")

                // SQL Delight
                implementation("com.squareup.sqldelight:runtime:${Versions.sqldelight}")
                implementation("com.squareup.sqldelight:sqlite-driver:${Versions.sqldelight}")
            }
        }
    }
}
