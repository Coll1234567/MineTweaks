package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.joml.Math;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Direction;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class DoubleDoorOpenTweak extends Tweak {

    @Path("enable-for-players")
    @Comment("Makes double doors open together when opened by players")
    private boolean enablePlayer = true;

    @Path("enable-for-redstone")
    @Comment("Makes double doors open together when opened by redstone")
    private boolean enableRedstone = true;

    public DoubleDoorOpenTweak() {
        super("double-door-opening", Category.BLOCK);
        this.description = List
                .of("<gray>Makes both sides of a double door open and close together.", "",
                        "<gray>Enabled For Players: %player%",
                        "<gray>Enabled For Redstone: %redstone%");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
        bus.subscribe(BlockRedstoneEvent.class, this::onBlockRedstone);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (!this.enablePlayer || event.useInteractedBlock() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_BLOCK
                || !Tag.WOODEN_DOORS.isTagged(block.getType())) {
            return;
        }

        Door door = (Door) block.getBlockData();
        handleConnectedDoor(block, door, !door.isOpen(), false);
    }

    private void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        if (!this.enableRedstone || !Tag.DOORS.isTagged(block.getType())) {
            return;
        }

        int current = event.getNewCurrent();
        Door door = (Door) block.getBlockData();
        boolean matching = handleConnectedDoor(block, door, current > 0, true) == door.isOpen();

        if (matching) {
            if (door.isOpen()) {
                event.setNewCurrent(Math.max(1, current));
            } else {
                event.setNewCurrent(0);
            }
        }
    }

    private boolean handleConnectedDoor(Block block, Door source, boolean open, boolean redstone) {
        Block otherDoor = getConnectedDoor(block, source);
        if (otherDoor == null || (otherDoor.getType() == Material.IRON_DOOR && !redstone)) {
            return open;
        }

        if (redstone && (block.getBlockPower() > 0 || otherDoor.getBlockPower() > 0)) {
            open = true;
        }

        Door other = (Door) otherDoor.getBlockData();
        if (other.isOpen() != open) {
            other.setOpen(open);
            otherDoor.setBlockData(other);
        }

        return open;
    }

    private Block getConnectedDoor(Block block, Door door) {
        Direction direction = Direction.fromBlockFace(door.getFacing());
        direction = door.getHinge() == Hinge.LEFT ? direction.rotateClockwiseY() : direction.rotateCounterClockwiseY();
        Block adjacent = block.getRelative(direction.getBlockFace());
        if (!(adjacent.getBlockData() instanceof Door other) || door.getHalf() != other.getHalf() ||
                door.getFacing() != other.getFacing() || door.getHinge() == other.getHinge()) {
            return null;
        }

        return adjacent;
    }
}
