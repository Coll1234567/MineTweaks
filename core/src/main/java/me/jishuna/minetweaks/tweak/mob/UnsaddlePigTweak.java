package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class UnsaddlePigTweak extends Tweak {

    public UnsaddlePigTweak() {
        super("unsaddle-pigs", Category.MOB);
        this.description = List.of(ChatColor.GRAY + "Allows players to remove saddles from pigs by right clicking them with an empty hand while sneaking.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEntityEvent.class, this::onEntityInteract);
    }

    private void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Pig pig)) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isSneaking() || !pig.hasSaddle() || !player.getEquipment().getItemInMainHand().getType().isAir()) {
            return;
        }

        event.setCancelled(true);
        pig.setSaddle(false);
        player.getEquipment().setItemInMainHand(new ItemStack(Material.SADDLE));
    }
}
