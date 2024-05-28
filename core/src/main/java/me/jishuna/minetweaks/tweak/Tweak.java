package me.jishuna.minetweaks.tweak;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.ChatPaginator;
import me.jishuna.jishlib.Plugin;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.config.ReloadableInstanceDataHolder;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.inventory.InventorySession;
import me.jishuna.jishlib.util.StringUtils;

public abstract class Tweak implements Listener, Comparable<Tweak> {
    private static final File TWEAK_FOLDER = new File(Plugin.getInstance().getDataFolder(), "Tweaks");

    @Path("enabled")
    @Comment("Allows you to fully enable or disable this tweak")
    protected boolean enabled = true;

    @Path("display-name")
    @Comment("The name of this tweak as seen in game.")
    protected String displayName = "Unnamed Tweak";

    @Path("description")
    @Comment("The description of this tweak as seen in game.")
    protected List<String> description = List.of(ChatColor.GRAY + "No Description");

    private final String name;
    private final Category category;

    protected Tweak(String name, Category category) {
        this.name = name;
        this.category = category;

        this.displayName = StringUtils.capitalizeAll(name.replace('-', ' '));
    }

    public void reload() {
        new ReloadableInstanceDataHolder<>(new File(TWEAK_FOLDER, this.category.name().toLowerCase() + File.separator + this.name + ".yml"), this)
                .save(false)
                .load();

        Map<String, Object> placeholders = getPlaceholders();
        this.description = this.description
                .stream()
                .map(line -> ChatPaginator.wordWrap(line, 40))
                .flatMap(Arrays::stream)
                .map(string -> replacePlaceholders(string, placeholders))
                .toList();
    }

    protected void registerEvents(EventBus bus) {
    }

    public void onMenuClick(InventoryClickEvent event, InventorySession session) {
        // Do nothing by default
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public Map<String, Object> getPlaceholders() {
        return Collections.emptyMap();
    }

    @Override
    public int compareTo(Tweak other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tweak other)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    private String replacePlaceholders(String input, Map<String, Object> placeholders) {
        for (Entry<String, Object> entry : placeholders.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue().toString());
        }

        return input;
    }
}
