package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class DownwardsLadderPlacementTweak extends Tweak implements ToggleableTweak {

    @Path("max-distance")
    @Comment("The maximum distance ladders can be expanded downwards")
    private int maxDistance = 64;

    public DownwardsLadderPlacementTweak() {
        super("downwards-ladder-placement", Category.BLOCK);
        this.description = List
                .of("<gray>Allows building ladders downwards by right clicking on a ladder while holding a ladder.",
                        "<gray>Requires a valid space below the set of ladders to place another ladder.", "",
                        "<gray>Maximum Distance: %max-distance%");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onInteract);
    }

    private void onInteract(PlayerInteractEvent event) {
        if (!isEnabled(event.getPlayer()) || event.useInteractedBlock() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        if (item == null || block.getType() != Material.LADDER || item.getType() != Material.LADDER) {
            return;
        }

        Directional current = (Directional) block.getBlockData();
        Block target = getNextPlacement(block);

        if (!Tag.REPLACEABLE.isTagged(target.getType()) || !current.isSupported(target)) {
            return;
        }

        BlockPlaceEvent placeEvent = new BlockPlaceEvent(target, target.getState(), target.getRelative(current.getFacing().getOppositeFace()), item, event.getPlayer(), true, event.getHand());
        Bukkit.getPluginManager().callEvent(placeEvent);
        if (placeEvent.isCancelled()) {
            return;
        }

        target.setBlockData(current);

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    private Block getNextPlacement(Block source) {
        int minHeight = source.getWorld().getMinHeight();
        for (int distance = 0; source.getY() > minHeight && source.getType() == Material.LADDER && distance < this.maxDistance; distance++) {
            source = source.getRelative(BlockFace.DOWN);
        }

        return source;
    }
}
