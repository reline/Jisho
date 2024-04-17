tasks.register("clean", Delete::class) {
    rootProject.layout.buildDirectory.get().asFile.delete()
}

// todo: forward cli params
tasks.named("dependencies").configure {
    dependsOn(subprojects.map { it.tasks.dependencies })
    dependsOn(gradle.includedBuilds.map { it.task(":dependencies") })
}
