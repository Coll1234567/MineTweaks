package me.jishuna.minetweaks.tweak.misc;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.JishLib;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class InventoryCraftingTweak extends Tweak {

    public InventoryCraftingTweak() {
        super("inventory-crafting-table", Category.MISC);
        this.description = List.of(ChatColor.GRAY + "Allows players to use crafting tables from their inventory with the swap hand key (Default: F).");
    }

    @EventHandler(ignoreCancelled = true)
    private void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (event.getInventory().getType() != InventoryType.CRAFTING || event.getClick() != ClickType.SWAP_OFFHAND
                || item == null || item.getType() != Material.CRAFTING_TABLE) {
            return;
        }

        event.setCancelled(true);
        JishLib.run(() -> event.getWhoClicked().openWorkbench(null, true));
    }
}
