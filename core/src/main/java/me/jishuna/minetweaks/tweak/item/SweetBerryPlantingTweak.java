package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class SweetBerryPlantingTweak extends Tweak implements ToggleableTweak {

    public SweetBerryPlantingTweak() {
        super("sweet-berry-planting", Category.ITEM);
        this.description = List.of(ChatColor.GRAY + "Stops players from planting sweet berries when trying to eat them, players must sneak to plant them.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!isEnabled(player) || event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null
                || item.getType() != Material.SWEET_BERRIES || player.isSneaking()) {
            return;
        }

        event.setUseInteractedBlock(Result.DENY);
    }
}
