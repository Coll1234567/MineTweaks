package me.jishuna.minetweaks.util;

import org.bukkit.block.Block;

/**
 * Thanks Choco <br>
 * <a href=
 * "https://github.com/2008Choco/VeinMiner/blob/master/veinminer-common/src/main/java/wtf/choco/veinminer/util/BlockFace.java">Source</a>
 */
public enum BlockFace {
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_EAST(1, 0, -1),
    NORTH_WEST(-1, 0, -1),
    SOUTH_EAST(1, 0, 1),
    SOUTH_WEST(-1, 0, 1),
    NORTH_UP(0, 1, -1),
    EAST_UP(1, 1, 0),
    SOUTH_UP(0, 1, 1),
    WEST_UP(-1, 1, 0),
    NORTH_DOWN(0, -1, -1),
    EAST_DOWN(1, -1, 0),
    SOUTH_DOWN(0, -1, 1),
    WEST_DOWN(-1, -1, 0);

    private final int xOffset;
    private final int yOffset;
    private final int zOffset;

    private BlockFace(int xOffset, int yOffset, int zOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public Block getRelative(Block block) {
        return block.getRelative(this.xOffset, this.yOffset, this.zOffset);
    }
}
