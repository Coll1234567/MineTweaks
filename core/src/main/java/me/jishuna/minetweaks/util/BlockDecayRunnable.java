package me.jishuna.minetweaks.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockDecayRunnable extends BukkitRunnable {
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final Set<Block> processing;
    private final List<Block> blocks;
    private final int blocksPerTick;

    public BlockDecayRunnable(Set<Block> processing, List<Block> blocks) {
        this.processing = processing;
        this.blocks = blocks;
        this.blocksPerTick = (int) Math.ceil(blocks.size() / 100d);
    }

    @Override
    public void run() {
        for (int i = 0; i < this.blocksPerTick; i++) {
            Block block = this.blocks.remove(this.random.nextInt(this.blocks.size()));
            this.processing.remove(block);

            if (Tag.LEAVES.isTagged(block.getType())) {
                block.breakNaturally();
            }

            if (this.blocks.isEmpty()) {
                cancel();
                return;
            }
        }
    }
}
