plugins {
    id("java")
	id("io.github.goooler.shadow") version "8.1.7"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
	implementation(project(":core"))	
}

tasks.shadowJar {
	archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.assemble {
	dependsOn(tasks.shadowJar)
	finalizedBy(tasks.named("copyJar"))
}

tasks.register("copyJar", Copy::class) {
    doNotTrackState("")
    val target = System.getenv("plugin-dir")

    if (target != null) {
        from(tasks.shadowJar)
        into(File(target))
    }
}