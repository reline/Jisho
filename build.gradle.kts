plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.wire) apply false
    id("com.github.reline.jisho.prepopulator") apply false
}

tasks.register("clean", Delete::class) {
    rootProject.layout.buildDirectory.get().asFile.delete()
}

// todo: forward cli params
tasks.named("dependencies").configure {
    dependsOn(subprojects.map { it.tasks.dependencies })
    dependsOn(
        gradle.includedBuilds.filterNot { it.name == rootProject.name }
            .map { it.task(":dependencies") }
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
