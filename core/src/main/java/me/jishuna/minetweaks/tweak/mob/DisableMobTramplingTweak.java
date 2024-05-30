package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityInteractEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class DisableMobTramplingTweak extends Tweak {
    public DisableMobTramplingTweak() {
        super("disable-mob-trampling", Category.MOB);
        this.description = List
                .of(ChatColor.GRAY + "Prevents mobs from trampling farmland.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityInteractEvent.class, this::onEntityTrample);
    }

    private void onEntityTrample(EntityInteractEvent event) {
        if (event.getEntityType() != EntityType.PLAYER && event.getBlock().getType() == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }
}
