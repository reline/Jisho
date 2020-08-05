plugins {
    id("application")
    kotlin("jvm")
    kotlin("kapt")

    id("com.github.johnrengelman.shadow")
}

repositories {
    jcenter()
    mavenCentral()
}

application {
    mainClassName = "com.github.reline.jisho.JishoDBKt"
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

    implementation("org.xerial:sqlite-jdbc:3.21.0.1")
    implementation("com.squareup.sqldelight:sqlite-driver:${Versions.sqldelight}")

    implementation(files("libs/tikxml-core-issue-131-SNAPSHOT.jar"))
    implementation ("com.tickaroo.tikxml:annotation:0.8.16-SNAPSHOT") {
        exclude(group = "com.tickaroo.tikxml", module = "core")
    }
    kapt("com.tickaroo.tikxml:processor-common:0.8.16-SNAPSHOT")
    kapt("com.tickaroo.tikxml:processor:0.8.16-SNAPSHOT")

    implementation("com.squareup.moshi:moshi:${Versions.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    implementation("com.squareup.okio:okio:${Versions.okio}")

    testImplementation("junit:junit:4.12")
}

// https://youtrack.jetbrains.com/issue/KT-29082
tasks.create<JavaExec>("execute") {
    main = "com.github.reline.jisho.JishoDBKt"
    classpath = objects.fileCollection().from(
            tasks.named("compileKotlin"),
            tasks.named("compileJava"),
            configurations.named("runtimeClasspath")
    )
}