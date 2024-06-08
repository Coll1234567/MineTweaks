package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import org.bukkit.Tag;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class WearableBannersTweak extends Tweak {

    public WearableBannersTweak() {
        super("wearable-banners", Category.ITEM);
        this.description = List.of("<gray><!i>Allows players to wear any type of banner on their head.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(InventoryClickEvent.class, this::onClick);
    }

    private void onClick(InventoryClickEvent event) {
        if (event.getRawSlot() != 5 || !(event.getClickedInventory() instanceof PlayerInventory inventory)) {
            return;
        }

        ItemStack banner = event.getCursor();
        if (!Tag.BANNERS.isTagged(banner.getType())) {
            return;
        }

        event.getView().setCursor(event.getCurrentItem());
        inventory.setHelmet(banner);
        event.setCancelled(true);
    }
}
