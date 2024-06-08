package me.jishuna.minetweaks.tweak.armorstand;

import java.util.List;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.EntityPlaceEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class ArmorStandArmsTweak extends Tweak {

    public ArmorStandArmsTweak() {
        super("armor-stand-arms", Category.ARMOR_STAND);
        this.description = List
                .of("<gray><!i>Automatically gives armor stands arms when a player places them.",
                        "<gray><!i>This allows players to display items in their hands.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(EntityPlaceEvent.class, this::onEntityPlace);
    }

    private void onEntityPlace(EntityPlaceEvent event) {
        if (event.getEntity() instanceof ArmorStand stand) {
            stand.setArms(true);
        }
    }
}
