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
