plugins {
    base
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `java-gradle-plugin`

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

val functionalTest by sourceSets.creating
val functionalTestTask = tasks.register<Test>("functionalTest") {
    group = "verification"
    testClassesDirs = functionalTest.output.classesDirs
    classpath = functionalTest.runtimeClasspath
}

tasks.check {
    dependsOn(functionalTestTask)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("jisho") {
            id = "com.github.reline.jisho.database"
            implementationClass = "com.github.reline.jisho.JishoDatabasePlugin"
        }
    }
    testSourceSets(functionalTest)
}

dependencies {
    implementation(gradleApi())

    implementation(libs.jisho.database)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.sqlite.jdbc)
    implementation(libs.sqldelight.sqlite.driver)

    implementation(libs.tikxml.core)
    implementation(libs.tikxml.annotation)
    kapt(libs.tikxml.processor)
    kapt(libs.tikxml.processorCommon)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.moshi)
    kapt(libs.moshi.codegen)
    implementation(libs.okio)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.okio.fakefilesystem)
    testImplementation(libs.okhttp.mockwebserver)

    "functionalTestImplementation"(libs.junit.jupiter)
    "functionalTestRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}
