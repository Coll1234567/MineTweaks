package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import me.jishuna.jishlib.Constants;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Components;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class LowDurabilityWarningTweak extends Tweak implements ToggleableTweak {

    @Path("threshold")
    @Comment("Items must have less durability than this value remaining to trigger the warning")
    private int threshold = 10;

    @Path("message")
    @Comment("Allows changing the formatting of the warning message")
    private String message = "<gold>Your <dark_aqua><item> <gold>only has <red><amount> <gold>durability remaining!";

    public LowDurabilityWarningTweak() {
        super("low-durability-warning", Category.ITEM);
        this.description = List.of(ChatColor.GRAY + "Warns players when an items durability is getting low.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerItemDamageEvent.class, this::onItemDamage);
    }

    private void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (!isEnabled(player) || !(item.getItemMeta() instanceof Damageable damageable)) {
            return;
        }

        int remaining = item.getType().getMaxDurability() - damageable.getDamage() - event.getDamage();
        if (remaining > this.threshold || remaining <= 0) {
            return;
        }

        Component itemComponent = damageable.hasDisplayName() ? Component.text(damageable.getDisplayName()) : Component.translatable(item.getTranslationKey());

        Component component = Constants.MINI_MESSAGE
                .deserialize(this.message,
                        Placeholder.component("item", itemComponent),
                        Formatter.number("amount", remaining));

        Components.sendMessage(player, component);
    }
}
