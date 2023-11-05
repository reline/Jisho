//repositories {
//    mavenCentral()
//    maven {
//        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
//    }
//    maven {
//        url = uri("https://www.atilika.org/nexus/content/repositories/atilika")
//    }
//    maven {
//        url = uri("https://repo.maven.apache.org/maven2")
//    }
//}

plugins {
    base
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `java-gradle-plugin`

    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("org.jetbrains.kotlin.kapt") version "1.9.10"
}

group = "com.github.reline.jisho.database"

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
            id = "com.github.reline.jisho"
            implementationClass = "com.github.reline.jisho.JishoPlugin"
        }
    }
    testSourceSets(functionalTest)
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlin.plugin)
    compileOnly(libs.android.plugin)

    implementation(project(":common"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.sqliteJdbc)
    implementation(libs.sqldelight.sqlite.driver)

    implementation(libs.tikxml.core)
    implementation(libs.tikxml.annotation)
    kapt(libs.tikxml.processor)
    kapt(libs.tikxml.processorCommon)

    implementation(libs.moshi)
    kapt(libs.moshi.codegen)
    implementation(libs.okio)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    "functionalTestImplementation"(libs.junit.jupiter)
    "functionalTestRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

