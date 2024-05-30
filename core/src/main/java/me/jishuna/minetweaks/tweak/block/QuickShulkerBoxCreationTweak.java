package me.jishuna.minetweaks.tweak.block;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class QuickShulkerBoxCreationTweak extends Tweak {

    public QuickShulkerBoxCreationTweak() {
        super("quick-shulker-box-creation", Category.BLOCK);
        this.description = List.of(ChatColor.GRAY + "Allows players to quickly convert containers into shulker boxes by right clicking them while holding a shulker shell in each hand.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Result.DENY || event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack hand = player.getEquipment().getItemInMainHand();
        ItemStack offhand = player.getEquipment().getItemInOffHand();

        if (hand.getType() != Material.SHULKER_SHELL || offhand.getType() != Material.SHULKER_SHELL) {
            return;
        }

        Block block = event.getClickedBlock();

        Container container = (Container) block.getState();
        Directional directional = (Directional) container.getBlockData();

        container.setBlockData(Material.SHULKER_BOX.createBlockData(data -> ((Directional) data).setFacing(directional.getFacing())));
        container.update(true);

        ((Container) block.getState()).getInventory().setContents(container.getSnapshotInventory().getContents());

        if (player.getGameMode() != GameMode.CREATIVE) {
            hand.setAmount(hand.getAmount() - 1);
            offhand.setAmount(offhand.getAmount() - 1);
        }
    }
}
