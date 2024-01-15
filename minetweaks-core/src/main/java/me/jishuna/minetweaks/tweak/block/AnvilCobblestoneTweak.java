package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class AnvilCobblestoneTweak extends Tweak {

    public AnvilCobblestoneTweak() {
        super("anvil-cobblestone-conversion", Category.BLOCK);
        this.description = List
                .of(ChatColor.GRAY + "Allows anvils to convert cobblestone into sand when landing on top of it.",
                        ChatColor.GRAY + "This allows for renewable sand without duplication exploits.");
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock block)) {
            return;
        }

        Material material = block.getBlockData().getMaterial();
        if (material != Material.ANVIL && material != Material.CHIPPED_ANVIL && material != Material.DAMAGED_ANVIL) {
            return;
        }

        Block target = event.getBlock().getRelative(BlockFace.DOWN);

        if (target.getType() == Material.COBBLESTONE) {
            target.setType(Material.SAND);
            target.getWorld().spawnParticle(Particle.BLOCK_DUST, target.getLocation().add(0.5, 0.5, 0.5), 25, 0.3, 0.3, 0.3, Material.COBBLESTONE.createBlockData());
        }
    }
}
