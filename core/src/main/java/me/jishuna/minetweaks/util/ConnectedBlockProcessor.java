package me.jishuna.minetweaks.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.block.Block;

public class ConnectedBlockProcessor {
    private final Block origin;
    private final int maxBlocks;
    private final Predicate<Block> predicate;
    private final BlockFace[] faces;

    public ConnectedBlockProcessor(Block origin, int maxBlocks, Predicate<Block> predicate) {
        this(origin, maxBlocks, predicate, BlockFace.values());
    }

    public ConnectedBlockProcessor(Block origin, int maxBlocks, Predicate<Block> predicate, BlockFace... faces) {
        this.origin = origin;
        this.maxBlocks = maxBlocks;
        this.predicate = predicate;
        this.faces = faces;
    }

    public Collection<Block> process() {
        Set<Block> blocks = new LinkedHashSet<>();
        List<Block> toCheck = new ArrayList<>(32);
        List<Block> buffer = new ArrayList<>(32);

        toCheck.add(this.origin);
        if (this.predicate.test(this.origin)) {
            blocks.add(this.origin);
        }

        while (true) {
            for (Block block : toCheck) {
                for (BlockFace face : this.faces) {
                    Block target = face.getRelative(block);
                    if (!blocks.contains(target) && this.predicate.test(target)) {
                        blocks.add(target);
                        if (blocks.size() >= this.maxBlocks) {
                            return blocks;
                        }

                        buffer.add(target);
                    }
                }
            }

            toCheck.clear();
            toCheck.addAll(buffer);
            buffer.clear();

            if (toCheck.isEmpty()) {
                break;
            }
        }
        return blocks;
    }
}
