package me.jishuna.minetweaks.tweak.mob;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class ChickenPluckingTweak extends Tweak {

    public ChickenPluckingTweak() {
        super("chicken-plucking", Category.MOB);
        this.description = List.of(ChatColor.GRAY + "Allows players to pluck feathers from a chicken by right clicking while sneaking, dealing half a heart of damage in the process.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEntityEvent.class, this::onInteract);
    }

    private void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getEquipment().getItemInMainHand();
        if (event.getHand() != EquipmentSlot.HAND || !player.isSneaking()
                || !item.getType().isAir() || !(event.getRightClicked() instanceof Chicken chicken)
                || !chicken.isValid() || chicken.getNoDamageTicks() > 0) {
            return;
        }

        chicken.damage(1);
        chicken.getWorld().dropItem(chicken.getLocation(), new ItemStack(Material.FEATHER));
    }
}
