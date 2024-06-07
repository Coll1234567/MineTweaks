package me.jishuna.minetweaks.tweak.farming;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class SugarcaneBonemealTweak extends Tweak {

    @Path("enable-for-players")
    @Comment("Lets players grow sugarcane using bonemeal")
    private boolean enablePlayer = true;

    @Path("enable-for-dispensers")
    @Comment("Lets dispensers grow sugarcane using bonemeal")
    private boolean enableDispenser = true;

    @Path("max-height")
    @Comment("The maximum height a sugarcane can be grown to using bonemeal")
    private int maxHeight = 5;

    public SugarcaneBonemealTweak() {
        super("sugarcane-bonemealing", Category.FARMING);
        this.description = List
                .of("<gray>Allows players and dispensers to grow cactus using bonemeal.", "",
                        "<gray>Enabled For Players: %player%",
                        "<gray>Enabled For Dispensers: %dispenser%",
                        "<gray>Max Growth Height: %max-height%");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
        bus.subscribe(BlockDispenseEvent.class, this::onBlockDispense);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (!this.enablePlayer) {
            return;
        }

        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null || item.getType() != Material.BONE_MEAL || block.getType() != Material.SUGAR_CANE) {
            return;
        }

        if (FarmingUtil.tryGrowTallPlant(block, this.maxHeight) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    private void onBlockDispense(BlockDispenseEvent event) {
        if (!this.enableDispenser || event.getBlock().getType() != Material.DISPENSER) {
            return;
        }

        ItemStack item = event.getItem();
        Directional directional = (Directional) event.getBlock().getBlockData();
        BlockFace face = directional.getFacing();
        Block block = event.getBlock().getRelative(face);

        if (item.getType() != Material.BONE_MEAL || block.getType() != Material.SUGAR_CANE) {
            return;
        }

        if (FarmingUtil.tryGrowTallPlant(block, this.maxHeight)) {
            ((Container) event.getBlock().getState()).getInventory().removeItem(new ItemStack(Material.BONE_MEAL));
        }
    }
}
