package me.jishuna.minetweaks.tweak.farming;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.util.Tasks;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.ToggleableTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class RightClickHarvestingTweak extends Tweak implements ToggleableTweak {
    private final List<Vector> positions = new ArrayList<>();

    @Path("harvestable")
    @Comment("List of blocks that can be harvested with a right click")
    private final Set<Material> harvestable = Set.of(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART);

    public RightClickHarvestingTweak() {
        super("right-click-harvesting", Category.FARMING);
        this.description = List.of("<gray><!i>Allows players to quickly harvest fully-grown crops by right clicking.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PlayerInteractEvent.class, this::onPlayerInteract);
        bus.subscribe(BlockDropItemEvent.class, this::onBlockDropItems);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (!isEnabled(player) || event.useInteractedBlock() == Result.DENY
                || event.getAction() != Action.RIGHT_CLICK_BLOCK || !this.harvestable.contains(block.getType())) {
            return;
        }

        if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() < ageable.getMaximumAge()) {
            return;
        }

        BlockData toPlace = block.getType().createBlockData();
        if (event.getHand() == EquipmentSlot.HAND) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }

        if (player.breakBlock(block)) {
            this.positions.add(block.getLocation().toVector());

            Tasks.run(() -> {
                if (toPlace.isSupported(block)) {
                    block.setBlockData(toPlace);
                }
            });
        }
    }

    private void onBlockDropItems(BlockDropItemEvent event) {
        if (!this.positions.remove(event.getBlock().getLocation().toVector())) {
            return;
        }

        Material toRemove = event.getBlockState().getBlockData().getPlacementMaterial();

        Iterator<Item> iterator = event.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            ItemStack stack = item.getItemStack();
            if (stack.getType() != toRemove) {
                continue;
            }

            if (stack.getAmount() <= 1) {
                iterator.remove();
            } else {
                stack.setAmount(stack.getAmount() - 1);
                item.setItemStack(stack);
            }
            return;
        }
    }
}
