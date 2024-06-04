rootProject.name = "MineTweaks"

gradle.rootProject {
    this.version = "1.0.0-SNAPSHOT"
    this.group = "me.jishuna"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/central")
		maven("https://repo.epicebic.xyz/public")
    }
}

include("core")