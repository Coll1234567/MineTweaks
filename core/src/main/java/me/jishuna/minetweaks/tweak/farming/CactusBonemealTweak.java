package me.jishuna.minetweaks.tweak.farming;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class CactusBonemealTweak extends Tweak {

    @Path("max-height")
    @Comment("The maximum height a cactus can be grown to using bonemeal")
    private int maxHeight = 5;

    public CactusBonemealTweak() {
        super("cactus-bonemealing", Category.FARMING);
        this.description = List
                .of("<gray>Allows players to grow cactus using bonemeal.", "",
                        "<gray>Max Growth Height: %max-height%");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null || item.getType() != Material.BONE_MEAL || block.getType() != Material.CACTUS) {
            return;
        }

        if (FarmingUtil.tryGrowTallPlant(block, this.maxHeight) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
