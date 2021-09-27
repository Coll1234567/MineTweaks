package me.jishuna.minetweaks.api.module;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.minetweaks.api.events.EventWrapper;

public class TweakModule {
	private final String name;
	private final JavaPlugin owningPlugin;
	
	private boolean enabled;
	private YamlConfiguration config;
	private YamlConfiguration lang;

	private final Multimap<Class<? extends Event>, EventWrapper<? extends Event>> handlerMap = ArrayListMultimap
			.create();

	private final Map<String, Submodule> submodules = new HashMap<>();

	public TweakModule(JavaPlugin plugin, String name) {
		this.name = name;
		this.owningPlugin = plugin;
		reload();
	}

	public void reload() {
		this.config = FileUtils.loadResource(this.owningPlugin, "modules/" + this.name + ".yml").orElseGet(() -> null);
		this.lang = FileUtils.loadResource(this.owningPlugin, "messages.yml").orElseGet(() -> null); 
		this.enabled = this.config == null ? false : this.config.getBoolean("enabled", true);
	}

	public <T extends Event> void addEventHandler(Class<T> type, Consumer<T> consumer) {
		this.handlerMap.put(type, new EventWrapper<>(type, consumer));
	}

	public <T extends Event> Collection<EventWrapper<? extends Event>> getEventHandlers(Class<T> type) {
		return this.handlerMap.get(type);
	}

	public void addSubModule(String key) {
		Submodule module = new Submodule(key, this.lang.getString(this.name + "." + key + ".name"), this.lang.getString(this.name + "." + key + ".description"));	
		this.submodules.put(module.getKey(), module);
	}

	public Collection<Submodule> getSubModules() {
		return this.submodules.values();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return name;
	}
	
	public String getFriendlyName() {
		return this.lang.getString(this.name + ".name");
	}

	public JavaPlugin getOwningPlugin() {
		return owningPlugin;
	}

	// config methods
	public boolean getBoolean(String key, boolean def) {
		return !this.enabled ? false : this.config.getBoolean(key, def);
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	public int getInt(String key, int def) {
		return !this.enabled ? def : this.config.getInt(key, def);
	}

	public String getString(String key, String def) {
		return !this.enabled ? def : this.config.getString(key, def);
	}

	public List<String> getStringList(String key) {
		return !this.enabled ? Collections.emptyList() : this.config.getStringList(key);
	}
}
