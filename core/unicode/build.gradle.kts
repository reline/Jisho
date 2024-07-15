plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "io.github.reline.jisho"

dependencies {
    implementation(libs.birchlabs.ve)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.coroutines.test)
}
