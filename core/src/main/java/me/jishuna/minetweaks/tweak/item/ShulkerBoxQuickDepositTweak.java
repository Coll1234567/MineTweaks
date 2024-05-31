package me.jishuna.minetweaks.tweak.item;

import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Tag;
import org.bukkit.block.Container;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.inventory.Inventories;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class ShulkerBoxQuickDepositTweak extends Tweak {

    public ShulkerBoxQuickDepositTweak() {
        super("shulker-box-quick-deposit", Category.ITEM);
        this.description = List.of(ChatColor.GRAY + "Allows players to quickly deposit items into shulker boxes in their inventory by right clicking.");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(InventoryClickEvent.class, this::onClick);
    }

    private void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (event.getClick() != ClickType.RIGHT || item == null ||
                !Tag.SHULKER_BOXES.isTagged(item.getType()) || Inventories.getSession(event.getWhoClicked()) != null) {
            return;
        }

        ItemStack held = event.getCursor();
        if (held == null || held.getType().isAir() || Tag.SHULKER_BOXES.isTagged(held.getType())) {
            return;
        }

        event.setCancelled(true);

        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
        Container container = (Container) meta.getBlockState();
        HashMap<Integer, ItemStack> remaining = container.getSnapshotInventory().addItem(held);
        meta.setBlockState(container);
        item.setItemMeta(meta);

        if (remaining.isEmpty()) {
            event.getWhoClicked().setItemOnCursor(null);
        } else {
            event.getWhoClicked().setItemOnCursor(remaining.values().stream().findFirst().orElse(null));
        }
    }
}
