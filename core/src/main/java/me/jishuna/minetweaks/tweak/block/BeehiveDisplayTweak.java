package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.Constants;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Components;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class BeehiveDisplayTweak extends Tweak {

    @Path("message")
    @Comment("Allows changing the format of the message sent to players")
    private String message = "<gold>Honey: <yellow><honey>/<max_honey>   <gold>Bees: <yellow><bees>/<max_bees>";

    public BeehiveDisplayTweak() {
        super("beehive-display", Category.BLOCK);
        this.description = List.of("<gray>Right clicking on a beehive or bee nest with an empty hand will show information about the amount of honey and number of bees within the hive.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || event.useInteractedBlock() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item != null && !item.getType().isAir()) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block.getBlockData() instanceof Beehive hiveData && block.getState() instanceof org.bukkit.block.Beehive hiveState) {
            Component component = Constants.MINI_MESSAGE
                    .deserialize(this.message,
                            Formatter.number("honey", hiveData.getHoneyLevel()),
                            Formatter.number("max_honey", hiveData.getMaximumHoneyLevel()),
                            Formatter.number("bees", hiveState.getEntityCount()),
                            Formatter.number("max_bees", hiveState.getMaxEntities()));
            Components.sendActionBar(event.getPlayer(), component);
        }
    }
}
