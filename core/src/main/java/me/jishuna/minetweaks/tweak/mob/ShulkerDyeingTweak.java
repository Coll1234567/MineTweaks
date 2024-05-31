package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Dye;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class ShulkerDyeingTweak extends Tweak {

    public ShulkerDyeingTweak() {
        super("shulker-dyeing", Category.MOB);
        this.description = List.of(ChatColor.GRAY + "Allows players to dye living shulkers using any color of dye.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEntityEvent.class, this::onEntityInteract);
    }

    private void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        ItemStack item;
        if (event.getHand() == EquipmentSlot.HAND) {
            item = player.getEquipment().getItemInMainHand();
        } else {
            item = player.getEquipment().getItemInOffHand();
        }

        if (!(event.getRightClicked() instanceof Shulker shulker)) {
            return;
        }

        Dye dye = Dye.fromItem(item);
        if (dye == null || shulker.getColor() == dye.getDyeColor()) {
            return;
        }

        event.setCancelled(true);
        shulker.setColor(dye.getDyeColor());

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
