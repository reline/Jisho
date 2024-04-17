tasks.register("clean", Delete::class) {
    rootProject.layout.buildDirectory.get().asFile.delete()
}
