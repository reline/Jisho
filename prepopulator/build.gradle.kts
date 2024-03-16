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
    description = "Runs the functional tests."
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
            id = "com.github.reline.jisho.prepopulator"
            implementationClass = "com.github.reline.jisho.JishoDatabasePopulatorPlugin"
        }
    }
    testSourceSets(functionalTest)
}

tasks.withType<JavaCompile>().configureEach {
    classpath += files("src/main/resources")
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

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.mockk)
    testImplementation(libs.okio.fakefilesystem)
    testImplementation(libs.okhttp.mockwebserver)

    "functionalTestImplementation"(kotlin("test"))
    "functionalTestImplementation"(libs.junit.jupiter)
    "functionalTestRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    "functionalTestImplementation"(libs.okio.fakefilesystem)
}
