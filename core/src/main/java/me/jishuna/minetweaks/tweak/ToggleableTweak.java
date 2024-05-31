package me.jishuna.minetweaks.tweak;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public interface ToggleableTweak {
    static final NamespacedKey KEY = NamespacedKey.fromString("mintweaks:disabled");

    default boolean toggle(Player player) {
        String name = ((Tweak) this).getName();
        List<String> disabled = player.getPersistentDataContainer().getOrDefault(KEY, PersistentDataType.LIST.strings(), new ArrayList<>());
        disabled = new ArrayList<>(disabled);

        boolean enabled = true;
        if (disabled.contains(name)) {
            disabled.remove(name);
        } else {
            disabled.add(name);
            enabled = false;
        }

        player.getPersistentDataContainer().set(KEY, PersistentDataType.LIST.strings(), disabled);
        return enabled;
    }

    default boolean isEnabled(Player player) {
        String name = ((Tweak) this).getName();
        List<String> disabled = player.getPersistentDataContainer().get(KEY, PersistentDataType.LIST.strings());

        return disabled == null || !disabled.contains(name);
    }
}
