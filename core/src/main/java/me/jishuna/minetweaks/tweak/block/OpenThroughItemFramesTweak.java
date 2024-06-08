package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class OpenThroughItemFramesTweak extends Tweak implements ToggleableTweak {

    public OpenThroughItemFramesTweak() {
        super("open-through-item-frames", Category.BLOCK);
        this.description = List
                .of("<gray><!i>Allows opening containers that item frames are attached to by right clicking them.",
                        "<gray><!i>Sneaking will allow interacting with the item frame as normal.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEntityEvent.class, this::onInteractEntity);
    }

    private void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!isEnabled(player) || player.isSneaking() || (!(event.getRightClicked() instanceof ItemFrame frame))) {
            return;
        }

        Block attached = frame.getLocation().getBlock().getRelative(frame.getAttachedFace());

        if (!(attached.getState() instanceof Container container)) {
            return;
        }

        player.openInventory(container.getInventory());
        event.setCancelled(true);
    }
}
