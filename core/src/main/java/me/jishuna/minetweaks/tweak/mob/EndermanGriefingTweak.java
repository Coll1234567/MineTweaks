package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class EndermanGriefingTweak extends Tweak {

    public EndermanGriefingTweak() {
        super("disable-enderman-griefing", Category.MOB);
        this.description = List.of("<gray><!i>Prevents enderman from picking up or placing blocks.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityChangeBlockEvent.class, this::onBlockChange);
    }

    private void onBlockChange(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        }
    }
}
