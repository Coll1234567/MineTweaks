package me.jishuna.minetweaks.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockColumnProcessor {
    private int height = 1;
    private Block topBlock;

    public BlockColumnProcessor(Block start) {
        this.topBlock = start;

        process(start);
    }

    private void process(Block start) {
        Material type = start.getType();
        Block down = start.getRelative(BlockFace.DOWN);

        while (down.getType() == type) {
            this.height++;
            down = down.getRelative(BlockFace.DOWN);
        }

        Block up = start.getRelative(BlockFace.UP);
        while (up.getType() == type) {
            this.topBlock = up;
            this.height++;
            up = up.getRelative(BlockFace.UP);
        }
    }

    public int getHeight() {
        return this.height;
    }

    public Block getTopBlock() {
        return this.topBlock;
    }
}
