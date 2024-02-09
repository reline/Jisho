plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sqldelight)
}

group = "com.github.reline.jisho"

sqldelight {
    databases {
        create("JishoDatabase") {
            packageName = "com.github.reline.jisho.sql"
            schemaOutputDirectory = file("src/main/sqldelight/com/github/reline/jisho/sql/migrations")
            dialect(libs.sqldelight.dialects.sqlite)
        }
    }
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "com.github.reline.jisho.common")
    }
}

dependencies {
    api(libs.kotlin.coroutines.core)
    api(libs.sqldelight.coroutines)
    implementation("uk.co.birchlabs.ve:ve")

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.sqldelight.runtime)
    testImplementation(libs.sqldelight.sqlite.driver)
}
