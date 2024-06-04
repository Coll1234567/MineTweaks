package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class OpenThroughSignsTweak extends Tweak implements ToggleableTweak {

    public OpenThroughSignsTweak() {
        super("open-through-signs", Category.BLOCK);
        this.description = List
                .of("<gray>Allows opening containers that signs are attached to by right clicking them.",
                        "<gray>Sneaking will allow interacting with the sign as normal.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isEnabled(player)) {
            return;
        }

        Block block = event.getClickedBlock();
        if (event.useInteractedBlock() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                player.isSneaking() || !Tag.WALL_SIGNS.isTagged(block.getType())) {
            return;
        }

        Directional directional = (Directional) block.getBlockData();
        Block attached = block.getRelative(directional.getFacing().getOppositeFace());

        if (!(attached.getState() instanceof Container container)) {
            return;
        }

        player.openInventory(container.getInventory());
        event.setCancelled(true);
    }
}
