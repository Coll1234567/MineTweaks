package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.pdc.PDCTypes;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class WitherMinionsTweak extends Tweak {
    private static final NamespacedKey KEY = new NamespacedKey("minetweaks", "spawned_minions");

    @Path("amount")
    @Comment("The amount of wither skeletons that should be spawned")
    private int amount = 3;

    public WitherMinionsTweak() {
        super("wither-minions", Category.MOB);
        this.description = List.of("<gray>Withers spawn wither skeletons when reduced to half health.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityDamageEvent.class, this::onDamage);
    }

    private void onDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Wither wither)) {
            return;
        }

        double health = wither.getHealth() - event.getFinalDamage();
        if (wither.getPersistentDataContainer().has(KEY, PDCTypes.BYTE) || health <= 0
                || health > (wither.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2)) {
            return;
        }

        wither.getPersistentDataContainer().set(KEY, PDCTypes.BYTE, (byte) 1);

        for (int i = 0; i < this.amount; i++) {
            wither.getWorld().spawn(wither.getLocation(), WitherSkeleton.class);
        }
    }

}
