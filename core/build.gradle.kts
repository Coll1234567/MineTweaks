import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
	id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    implementation("me.jishuna:Jishlib:3.0.0-SNAPSHOT")
}

bukkit {
	name = rootProject.name
	version = rootProject.version.toString()
	main = "me.jishuna.minetweaks.MineTweaks"
	apiVersion = "1.18"
}