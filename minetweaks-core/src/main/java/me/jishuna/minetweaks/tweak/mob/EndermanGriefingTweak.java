package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class EndermanGriefingTweak extends Tweak {

    public EndermanGriefingTweak() {
        super("disable-enderman-griefing", Category.MOB);
        this.description = List.of(ChatColor.GRAY + "Prevents enderman from picking up or placing blocks.");
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockChange(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        }
    }
}
