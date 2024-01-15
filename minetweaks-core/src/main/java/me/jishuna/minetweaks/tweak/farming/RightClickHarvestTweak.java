package me.jishuna.minetweaks.tweak.farming;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import me.jishuna.jishlib.JishLib;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.ConfigEntry;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class RightClickHarvestTweak extends Tweak implements ToggleableTweak {
    private final List<Vector> positions = new ArrayList<>();

    @ConfigEntry("harvestable")
    @Comment("List of blocks that can be harvested with a right click")
    private final Set<Material> harvestable = Set.of(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART);

    public RightClickHarvestTweak() {
        super("right-click-harvesting", Category.FARMING);
        this.description = List.of(ChatColor.GRAY + "Allows players to quickly harvest fully-grown crops by right clicking.");
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (!isEnabled(player) || event.useInteractedBlock() == Result.DENY
                || event.getAction() != Action.RIGHT_CLICK_BLOCK || !this.harvestable.contains(block.getType())) {
            return;
        }

        if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() < ageable.getMaximumAge()) {
            return;
        }

        BlockData toPlace = block.getType().createBlockData();

        if (player.breakBlock(block)) {
            this.positions.add(block.getLocation().toVector());

            JishLib.run(() -> {
                if (toPlace.isSupported(block)) {
                    block.setBlockData(toPlace);
                }
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockDropItems(BlockDropItemEvent event) {
        if (!this.positions.remove(event.getBlock().getLocation().toVector())) {
            return;
        }

        Material toRemove = event.getBlockState().getBlockData().getPlacementMaterial();

        Iterator<Item> iterator = event.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            ItemStack stack = item.getItemStack();
            if (stack.getType() != toRemove) {
                continue;
            }

            if (stack.getAmount() <= 1) {
                iterator.remove();
            } else {
                stack.setAmount(stack.getAmount() - 1);
                item.setItemStack(stack);
            }
            return;
        }
    }
}
