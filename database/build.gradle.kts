plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sqldelight)
}

group = "com.github.reline.jisho"

sqldelight {
    databases {
        create("JishoDatabase") {
            packageName.set("com.github.reline.jisho.sql")
            schemaOutputDirectory.set(file("src/main/sqldelight/com/github/reline/jisho/sql/migrations"))
            dialect(libs.sqldelight.dialects.sqlite)
        }
    }
}

dependencies {
    api(libs.kotlin.coroutines.core)
    api(libs.sqldelight.coroutines)
    implementation(libs.birchlabs.ve)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.sqldelight.runtime)
    testImplementation(libs.sqldelight.sqlite.driver)
}
