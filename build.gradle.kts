tasks.register("clean", Delete::class) {
    rootProject.layout.buildDirectory.get().asFile.delete()
}

tasks.named("dependencies").configure {
    dependsOn(
        gradle.includedBuilds.filterNot { it.name == rootProject.name }
            .map { it.task(":dependencies") }
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.create("test") {
    group = "verification"
    dependsOn(
        gradle.includedBuild("build-logic").task(":test"),
        gradle.includedBuild("core").task(":unicode:test"),
        gradle.includedBuild("core").task(":database:test"),
        gradle.includedBuild("core").task(":database-readonly:test"),
        gradle.includedBuild("prepopulator").task(":test"),
        gradle.includedBuild("android").task(":check"),
    )
}
