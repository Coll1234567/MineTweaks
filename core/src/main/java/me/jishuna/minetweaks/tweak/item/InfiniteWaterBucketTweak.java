package me.jishuna.minetweaks.tweak.item;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import me.jishuna.jishlib.config.Comment;
import me.jishuna.jishlib.config.Path;
import me.jishuna.jishlib.event.EventBus;
import me.jishuna.jishlib.item.ItemBuilder;
import me.jishuna.jishlib.util.Tasks;
import me.jishuna.minetweaks.tweak.Category;
import me.jishuna.minetweaks.tweak.RegisterTweak;
import me.jishuna.minetweaks.tweak.Tweak;

@RegisterTweak
public class InfiniteWaterBucketTweak extends Tweak {
    private static final Enchantment INFINITY = Registry.ENCHANTMENT.get(NamespacedKey.minecraft("infinity"));

    @Path("level-cost")
    @Comment("The number of levels required to apply infinity to a water bucket")
    private int cost = 20;

    public InfiniteWaterBucketTweak() {
        super("infinite-water-bucket", Category.ITEM);
        this.description = List
                .of(ChatColor.GRAY + "Allows the creation of infinite water buckets by combining a water bucket with an infinity enchanted book in an anvil.", "",
                        ChatColor.GRAY + "Level Cost: %cost%");
    }

    @Override
    protected void registerEvents(EventBus bus) {
        bus.subscribe(PrepareAnvilEvent.class, this::onPrepareAnvil);
        bus.subscribe(PlayerBucketEmptyEvent.class, this::onBucketEmpty);
    }

    private void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);

        if (first == null || first.getType() != Material.WATER_BUCKET || first.containsEnchantment(INFINITY) ||
                second == null || second.getType() != Material.ENCHANTED_BOOK) {
            return;
        }

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) second.getItemMeta();
        if (meta.hasStoredEnchant(INFINITY)) {
            event.setResult(ItemBuilder.of(first.clone()).name(inventory.getRenameText()).enchant(INFINITY, 1).build());

            Tasks.run(() -> {
                inventory.setRepairCost(this.cost);
                inventory.setRepairCostAmount(1);
                inventory.getViewers().forEach(en -> ((Player) en).updateInventory());
            });
        }
    }

    private void onBucketEmpty(PlayerBucketEmptyEvent event) {
        EntityEquipment equipment = event.getPlayer().getEquipment();
        ItemStack item = equipment.getItem(event.getHand());
        if (item.getType() != Material.WATER_BUCKET) {
            return;
        }

        if (item.containsEnchantment(INFINITY)) {
            Tasks.run(() -> equipment.setItem(event.getHand(), item));
        }
    }
}
