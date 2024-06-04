package me.jishuna.minetweaks.tweak.misc;

import java.util.Iterator;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class AutoStepTweak extends Tweak {
    private static final AttributeModifier MODIFIER = new AttributeModifier("minetweaks:auto-step", 0.4, Operation.ADD_NUMBER);

    public AutoStepTweak() {
        super("auto-step", Category.MISC);
        this.description = List.of("<gray>Allows players to automatically step up one block.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerJoinEvent.class, this::onPlayerJoin);
        bus.subscribe(PlayerQuitEvent.class, this::onPlayerLeave);
    }

    private void onPlayerJoin(PlayerJoinEvent event) {
        removeModifier(event.getPlayer().getAttribute(Attribute.GENERIC_STEP_HEIGHT));
        event.getPlayer().getAttribute(Attribute.GENERIC_STEP_HEIGHT).addModifier(MODIFIER);
    }

    private void onPlayerLeave(PlayerQuitEvent event) {
        removeModifier(event.getPlayer().getAttribute(Attribute.GENERIC_STEP_HEIGHT));
    }

    private void removeModifier(AttributeInstance attribute) {
        Iterator<AttributeModifier> iterator = attribute.getModifiers().iterator();
        while (iterator.hasNext()) {
            AttributeModifier modifier = iterator.next();
            if (modifier.getName().equals(MODIFIER.getName())) {
                attribute.removeModifier(modifier);
                iterator.remove();
            }
        }
    }
}
