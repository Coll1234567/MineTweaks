package me.jishuna.minetweaks.tweak.mob;

import com.google.common.base.Objects;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.event.EventUtils;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class PetProtectionTweak extends Tweak implements ToggleableTweak {

    public PetProtectionTweak() {
        super("pet-protection", Category.MOB);
        this.description = List.of("<gray><!i>Prevents players from accidently hurting their pets with melee attacks or projectiles.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityDamageByEntityEvent.class, this::onDamage);
    }

    private void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Tameable tamable) || tamable.getOwner() == null) {
            return;
        }

        Entity damager = EventUtils.getSourceEntity(event);
        if (damager == null || !(damager instanceof Player player) || !isEnabled(player)) {
            return;
        }

        if (Objects.equal(player, tamable.getOwner())) {
            event.setCancelled(true);
        }
    }
}
