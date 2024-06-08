package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityResurrectEvent;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class TotemCooldownTweak extends Tweak {

    @Path("cooldown")
    @Comment("The cooldown (in ticks) on totems of undying.")
    private int cooldown = 5 * 20;

    public TotemCooldownTweak() {
        super("totem-cooldown", Category.ITEM);
        this.description = List
                .of("<gray><!i>Adds a cooldown to totems of undying, preventing them from being used in quick succession.", "",
                        "<gray><!i>Cooldown: <green><seconds> seconds.");
    }

    @Override
    public TagResolver[] getTagResolvers() {
        return new TagResolver[] {
                Formatter.number("ticks", this.cooldown),
                Formatter.number("seconds", this.cooldown / 20)
        };
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityResurrectEvent.class, this::onRessurect);
    }

    private void onRessurect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getCooldown(Material.TOTEM_OF_UNDYING) > 0) {
            event.setCancelled(true);
        } else {
            player.setCooldown(Material.TOTEM_OF_UNDYING, this.cooldown);
        }
    }
}
