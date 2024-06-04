package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class StickyPistonConversionTweak extends Tweak {

    public StickyPistonConversionTweak() {
        super("sticky-piston-conversion", Category.BLOCK);
        this.description = List.of("<gray>Allows players to quickly convert pistons into sticky pistons by right clicking them while holding a slimeball.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.useInteractedBlock() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getItem() == null) {
            return;
        }

        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        if (block.getType() == Material.PISTON && item.getType() == Material.SLIME_BALL) {
            Directional oldData = (Directional) block.getBlockData();

            block.setType(Material.STICKY_PISTON);
            Directional data = (Directional) block.getBlockData();
            data.setFacing(oldData.getFacing());
            block.setBlockData(data);

            if (player.getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }

            player.playSound(block.getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, 1f, 1f);
        }
    }
}
