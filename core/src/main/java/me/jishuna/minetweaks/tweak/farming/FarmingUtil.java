package me.jishuna.minetweaks.tweak.farming;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import me.jishuna.minetweaks.util.BlockColumnProcessor;

public class FarmingUtil {
    public static boolean tryGrowTallPlant(Block block, int maxHeight) {
        BlockColumnProcessor processor = new BlockColumnProcessor(block);
        int height = processor.getHeight();

        Block top = processor.getTopBlock();
        Ageable blockData = (Ageable) top.getBlockData();
        int age = blockData.getAge();

        if (height >= maxHeight) {
            return false;
        }

        if (age >= blockData.getMaximumAge()) {
            if (top.getY() >= top.getWorld().getMaxHeight() - 1) {
                return false;
            }

            top.getRelative(BlockFace.UP).setType(top.getType());
        } else {
            blockData.setAge(Math.min(age + ThreadLocalRandom.current().nextInt(2, 6), blockData.getMaximumAge()));
            top.setBlockData(blockData);
        }

        block.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, block.getLocation().add(0.5, 0.5, 0.5), 15, 0.3d, 0.3d, 0.3d);
        return true;
    }
}
