package me.jishuna.minetweaks.tweak.dispenser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Tasks;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class DispensersPlaceBlocksTweak extends Tweak {
    private static final Map<Material, Material> placementItems = new HashMap<>();

    static {
        for (Material material : Material.values()) {
            if (!material.isBlock()) {
                continue;
            }

            Material placementMaterial = material.createBlockData().getPlacementMaterial();
            if (placementMaterial != material) {
                placementItems.put(placementMaterial, material);
            }
        }
    }

    @Path("blacklist")
    @Comment("Items in this list will not be placeable by dispensers")
    private Set<Material> blacklist = Set.of(Material.TNT, Material.OBSIDIAN, Material.CRYING_OBSIDIAN);

    public DispensersPlaceBlocksTweak() {
        super("dispenser-block-placement", Category.DISPENSER);
        this.description = List.of(ChatColor.GRAY + "Allows dispensers to place blocks in front of them when powered.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(BlockDispenseEvent.class, this::onBlockDispense);
    }

    private void onBlockDispense(BlockDispenseEvent event) {
        if (event.getBlock().getType() != Material.DISPENSER) {
            return;
        }

        Directional directional = (Directional) event.getBlock().getBlockData();
        BlockFace face = directional.getFacing();
        Block block = event.getBlock().getRelative(face);

        Material original = event.getItem().getType();
        Material type = placementItems.getOrDefault(original, original);

        if (this.blacklist.contains(type) || !type.isBlock()) {
            return;
        }

        ItemStack removed = event.getItem().clone();
        event.setItem(new ItemStack(Material.AIR));

        if (block.getType().isAir() && type.createBlockData().isSupported(block)) {
            Tasks.run(() -> ((Container) event.getBlock().getState()).getInventory().removeItem(removed));
            block.setType(type);

            if (block.getBlockData() instanceof Directional dir && dir.getFaces().contains(directional.getFacing())) {
                dir.setFacing(directional.getFacing());
                block.setBlockData(dir);
            }
        }
    }
}
