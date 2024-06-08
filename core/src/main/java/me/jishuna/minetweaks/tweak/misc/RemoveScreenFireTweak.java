package me.jishuna.minetweaks.tweak.misc;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.potion.PotionEffectType;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class RemoveScreenFireTweak extends Tweak {

    public RemoveScreenFireTweak() {
        super("remove-screen-fire", Category.MISC);
        this.description = List.of("<gray><!i>Hides the on screen fire effect when players are immune to fire damage.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityCombustEvent.class, this::onCombust);
    }

    private void onCombust(EntityCombustEvent event) {
        if (event.getEntity() instanceof Player player
                && (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) || player.getGameMode() == GameMode.CREATIVE)) {
            event.setCancelled(true);
            player.setFireTicks(-20);
        }
    }
}
