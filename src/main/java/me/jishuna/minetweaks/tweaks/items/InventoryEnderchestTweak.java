package me.jishuna.minetweaks.tweaks.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.minetweaks.api.RegisterTweak;
import me.jishuna.minetweaks.api.tweak.Tweak;

@RegisterTweak(name = "inventory_enderchest")
public class InventoryEnderchestTweak extends Tweak {
	public InventoryEnderchestTweak(JavaPlugin plugin, String name) {
		super(plugin, name);

		addEventHandler(InventoryClickEvent.class, this::onClick);
	}

	@Override
	public void reload() {
		FileUtils.loadResource(getOwningPlugin(), "Tweaks/Items/" + this.getName() + ".yml").ifPresent(config -> {
			loadDefaults(config, false);
		});
	}

	private void onClick(InventoryClickEvent event) {
		if (event.getInventory().getType() != InventoryType.CRAFTING || event.getClick() != ClickType.SWAP_OFFHAND)
			return;

		ItemStack item = event.getCurrentItem();
		if (item == null || item.getType().isAir())
			return;

		if (item.getType() == Material.ENDER_CHEST) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(getOwningPlugin(),
					() -> event.getWhoClicked().openInventory(event.getWhoClicked().getEnderChest()));
		}
	}
}
