plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sqldelight)
}

group = "io.github.reline.jisho"

sqldelight {
    databases {
        create("JishoDatabase") {
            packageName.set("io.github.reline.jisho.db.readonly")
            dialect(libs.sqldelight.dialects.sqlite)
            dependency(projects.database)
        }
    }
}

dependencies {
    api(projects.database)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.sqldelight.runtime)
    testImplementation(libs.sqldelight.sqlite.driver)
    testImplementation(libs.sqldelight.coroutines)
}
